package com.example.recipe_app.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.example.recipe_app.R;
import com.example.recipe_app.adapters.RecipeRecyclerViewAdapter;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.util.Testing;
import com.example.recipe_app.viewmodel.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements RecipeRecyclerViewAdapter.RecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter recyclerViewAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        initComponents();
        subscribeObservers();
    }

    private void initComponents() {
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        initRecyclerView();
        initSearchView();
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerViewAdapter.displayLoading();
                searchRecipeApi(s, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewAdapter = new RecipeRecyclerViewAdapter(this);
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

    private void searchRecipeApi(String query, int numPage) {
        recipeListViewModel.searchRecipeApi(query, numPage);
    }

    @Override
    public void onRecipeClickListener(int index) {

    }
}
