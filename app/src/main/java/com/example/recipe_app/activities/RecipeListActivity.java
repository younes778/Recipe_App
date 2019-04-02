package com.example.recipe_app.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.recipe_app.R;
import com.example.recipe_app.adapters.RecipeRecyclerViewAdapter;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.util.Testing;
import com.example.recipe_app.viewmodel.RecipeListViewModel;

import java.util.List;

import static com.example.recipe_app.activities.RecipeDetailsActivity.EXTRA_RECIPE;

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
    }

    private void initComponents() {
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);

        initRecyclerView();
        initSearchView();
        subscribeObservers();
        initSupportActionBar();
    }

    private void initSupportActionBar(){
        setSupportActionBar((Toolbar)findViewById(R.id.tool_bar));
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerViewAdapter.displayLoading();
                searchRecipeApi(s, 1);
                searchView.clearFocus();
                
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)){
                    recipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void subscribeObservers() {
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    Testing.printRecipes(TAG, recipes);
                }
                recyclerViewAdapter.setRecipes(recipeListViewModel.getRecipes().getValue());
            }
        });
    }

    private void searchRecipeApi(String query, int numPage) {
        recipeListViewModel.searchRecipeApi(query, numPage);
    }

    @Override
    public void onRecipeClickListener(int index) {
        Intent i = new Intent(this,RecipeDetailsActivity.class);
        i.putExtra(EXTRA_RECIPE,recyclerViewAdapter.getSelectedRecipe(index));
        startActivity(i);
    }
}
