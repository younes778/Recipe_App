package com.example.recipe_app.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.recipe_app.R;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.requests.RecipeApi;
import com.example.recipe_app.requests.ServiceGenerator;
import com.example.recipe_app.requests.responses.RecipeSearchResponse;
import com.example.recipe_app.util.Testing;
import com.example.recipe_app.util.Utils;
import com.example.recipe_app.viewmodel.RecipeListViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        initComponents();
        subscribeObservers();

        testRecipeSearch();
    }

    private void initComponents() {
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes!=null)
                    Testing.printRecipes(TAG,recipes);
            }
        });
    }

    private void testRecipeSearch(){
        searchRecipeApi("chicken breast",1);

    }

    private void searchRecipeApi(String query,int numPage) {
        recipeListViewModel.searchRecipeApi(query,numPage);
    }
}
