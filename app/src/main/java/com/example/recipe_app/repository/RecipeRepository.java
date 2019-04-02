package com.example.recipe_app.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.requests.RecipeApiClient;
import com.example.recipe_app.util.Utils;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String query;
    private int pageNumber;
    private MutableLiveData<Boolean> queryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> recipesMediator = new MediatorLiveData<>();

    public static RecipeRepository getInstance() {
        if (instance == null)
            instance = new RecipeRepository();
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    public LiveData<List<Recipe>> getmRecipes() {
        return recipesMediator;
    }

    private void initMediators() {
        LiveData<List<Recipe>> recipeListSource = recipeApiClient.getmRecipes();
        recipesMediator.addSource(recipeListSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    recipesMediator.setValue(recipes);
                    doneQuery(recipes);
                } else {
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    public void doneQuery(List<Recipe> list) {
        if (list == null || list.size() % Utils.RECIPE_SEARCH_MAX_SIZE != 0) {
            queryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted() {
        return queryExhausted;
    }

    public LiveData<Recipe> getRecipe() {
        return recipeApiClient.getRecipe();
    }

    public void getRecipeApi(String recipeId) {
        recipeApiClient.getRecipeApi(recipeId);
    }

    public LiveData<Boolean> isRecipeRequestTimeOut() {
        return recipeApiClient.isRecipeRequestTimeOut();
    }

    public void searchRecipeApi(String query, int numPage) {
        if (numPage == 0) {
            numPage = 1;
        }
        this.query = query;
        pageNumber = numPage;
        queryExhausted.setValue(false);
        recipeApiClient.searchRecipeApi(query, numPage);
    }

    public void searchNextPage() {
        if (!queryExhausted.getValue()) {
            pageNumber++;
            searchRecipeApi(query, pageNumber);
        }
    }

    public void cancelRequest() {
        recipeApiClient.cancelRequest();
    }
}
