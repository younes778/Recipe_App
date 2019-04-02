package com.example.recipe_app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    public RecipeListViewModel(){
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getmRecipes();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return recipeRepository.isQueryExhausted();
    }

    public void searchRecipeApi(String query,int numPage) {
        recipeRepository.searchRecipeApi(query,numPage);
    }

    public void searchNextPage(){
        recipeRepository.searchNextPage();
    }
}
