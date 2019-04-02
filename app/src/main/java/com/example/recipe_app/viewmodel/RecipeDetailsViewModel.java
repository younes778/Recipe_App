package com.example.recipe_app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.repository.RecipeRepository;

import java.util.List;

public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private String recipeId;

    public RecipeDetailsViewModel(){
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return recipeRepository.getRecipe();
    }

    public void getRecipeApi(String recipeId) {
        this.recipeId = recipeId;
        recipeRepository.getRecipeApi(recipeId);
    }

    public String getRecipeId() {
        return recipeId;
    }

    public LiveData<Boolean> isRecipeRequestTimeOut(){
        return recipeRepository.isRecipeRequestTimeOut();
    }
}
