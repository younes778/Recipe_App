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

            }
        });
    }

    private void testRecipeSearch(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeSearchResponse> responseCall = recipeApi.searchRecipe(Utils.API_KEY_FOOD,
                "Chicken",
                "1");

        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "onResponse: server response "+response.code());
                if (response.code()==200){
                    Log.d(TAG, "onResponse: "+response.body().toString());
                    List<Recipe> recipes = response.body().getRecipes();
                    for (Recipe recipe:recipes){
                        Log.d(TAG, "onResponse: "+recipe.getTitle());
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: "+response.body().toString());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });

    }
}
