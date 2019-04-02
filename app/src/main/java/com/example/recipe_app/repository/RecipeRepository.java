package com.example.recipe_app.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;

    public static RecipeRepository getInstance() {
        if (instance==null)
            instance = new RecipeRepository();
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getmRecipes() {
        return recipeApiClient.getmRecipes();
    }

    public void searchRecipeApi(String query,int numPage) {
        if (numPage==0){
            numPage=1;
        }
        recipeApiClient.searchRecipeApi(query,numPage);
    }

    public void cancelRequest(){
        recipeApiClient.cancelRequest();
    }
}
