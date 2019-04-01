package com.example.recipe_app.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.recipe_app.R;
import com.example.recipe_app.adapters.RecipeRecyclerViewAdapter;
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

public class RecipeListActivity extends BaseActivity implements RecipeRecyclerViewAdapter.RecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter recyclerViewAdapter;

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
        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewAdapter = new RecipeRecyclerViewAdapter( this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void subscribeObservers() {
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    Testing.printRecipes(TAG, recipes);
                    recyclerViewAdapter.setRecipes(recipeListViewModel.getRecipes().getValue());
                }
            }
        });
    }

    private void testRecipeSearch() {
        searchRecipeApi("chicken breast", 1);

    }

    private void searchRecipeApi(String query, int numPage) {
        recipeListViewModel.searchRecipeApi(query, numPage);
    }

    @Override
    public void onRecipeClickListener(int index) {

    }
}
