package com.example.recipe_app.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe_app.R;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.viewmodel.RecipeDetailsViewModel;

public class RecipeDetailsActivity extends BaseActivity {
    private static final String TAG = "RecipeDetailsActivity";

    public static final String EXTRA_RECIPE = "recipe";
    private AppCompatImageView imageView;
    private TextView title,rank;
    private LinearLayout ingredientsContainer;
    private ScrollView scrollView;
    private RecipeDetailsViewModel recipeDetailsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        showProgress(true);
        initComponents();
        getIncomingIntent();
        subscribeObservers();
    }

    private void initComponents(){
        recipeDetailsViewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);
        imageView = findViewById(R.id.recipe_image);
        title = findViewById(R.id.recipe_title);
        rank = findViewById(R.id.recipe_social_score);
        ingredientsContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra(EXTRA_RECIPE)){
            Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
            recipeDetailsViewModel.getRecipeApi(recipe.getRecipe_id());
            Log.d(TAG, "getIncomingIntent: "+recipe.getTitle());
        }
    }

    private void subscribeObservers(){
        recipeDetailsViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe!=null){
                    if (recipe.getRecipe_id().equals(recipeDetailsViewModel.getRecipeId()))
                        setRecipeProperties(recipe);
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe){
        if (recipe!=null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(getBaseContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(imageView);
            title.setText(recipe.getTitle());
            rank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            ingredientsContainer.removeAllViews();

            for (String ingredient:recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                ingredientsContainer.addView(textView);
            }
        }
        showParemt();
        showProgress(false);
    }

    private void showParemt(){
        scrollView.setVisibility(View.VISIBLE);
    }
}
