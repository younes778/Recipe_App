package com.example.recipe_app.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String query;
    private int pageNumber;

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
        this.query = query;
        pageNumber = numPage;
        recipeApiClient.searchRecipeApi(query,numPage);
    }

    public void searchNextPage(){
        pageNumber++;
        searchRecipeApi(query,pageNumber);
    }

    public void cancelRequest(){
        recipeApiClient.cancelRequest();
    }
}
