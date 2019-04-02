package com.example.recipe_app.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.recipe_app.executors.AppExecutors;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.requests.responses.RecipeResponse;
import com.example.recipe_app.requests.responses.RecipeSearchResponse;
import com.example.recipe_app.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.recipe_app.util.Utils.NETWORK_TIMEOUT;
import static com.example.recipe_app.util.Utils.RECIPE_GET_TIMEOUT;

public class RecipeApiClient {
    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> recipe;
    private MutableLiveData<Boolean> recipeRequestTimeOut;

    private RetrieveRecipesRunnable retrieveRecipesRunnable;
    private RetrieveRecipeRunnable retrieveRecipeRunnable;

    public static RecipeApiClient getInstance() {
        if (instance == null)
            instance = new RecipeApiClient();
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        recipe = new MutableLiveData<>();
        recipeRequestTimeOut = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getmRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
    public LiveData<Boolean> isRecipeRequestTimeOut(){
        return recipeRequestTimeOut;
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if (retrieveRecipesRunnable!=null){
            retrieveRecipesRunnable = null;
        }
        retrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void getRecipeApi(String recipeId) {
        if (retrieveRecipeRunnable!=null){
            retrieveRecipeRunnable = null;
        }
        recipeRequestTimeOut.setValue(false);
        retrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);
        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                recipeRequestTimeOut.postValue(true);
                handler.cancel(true);
            }
        }, RECIPE_GET_TIMEOUT , TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Recipe> newRecipes = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber == 1) {
                        mRecipes.postValue(newRecipes);
                    } else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(newRecipes);
                        mRecipes.postValue(currentRecipes);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(Utils.API_KEY_FOOD, query, String.valueOf(pageNumber));
        }

        private void cancelRequest() {
            Log.d(TAG, "cancel search request: ");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if (retrieveRecipesRunnable !=null)
            retrieveRecipesRunnable.cancelRequest();
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String recipeId;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if (response.code() == 200) {
                    Recipe newRecipe = ((RecipeResponse) response.body()).getRecipe();
                        recipe.postValue(newRecipe);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    recipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                recipe.postValue(null);
            }

        }

        private Call<RecipeResponse> getRecipe(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(Utils.API_KEY_FOOD, recipeId);
        }
    }
}
