package com.example.recipe_app.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.recipe_app.executors.AppExecutors;
import com.example.recipe_app.model.Recipe;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.example.recipe_app.util.Utils.NETWORK_TIMEOUT;

public class RecipeApiClient {
    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> mRecipes;

    public static RecipeApiClient getInstance() {
        if (instance==null)
            instance = new RecipeApiClient();
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getmRecipes() {
        return mRecipes;
    }

    public void searchRecipeApi(){
        final Future handler = AppExecutors.getInstance().networkIO().submit(new Runnable() {
            @Override
            public void run() {

            }
        });

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {

                handler.cancel(true);
            }
        },NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}
