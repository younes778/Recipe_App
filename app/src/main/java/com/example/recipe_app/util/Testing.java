package com.example.recipe_app.util;

import android.util.Log;

import com.example.recipe_app.model.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(String tag, List<Recipe> recipes){
        for (Recipe recipe:recipes)
            Log.d(tag, recipe.getTitle());
    }
}
