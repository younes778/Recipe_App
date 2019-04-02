package com.example.recipe_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe_app.R;
import com.example.recipe_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecipeRecyclerViewAdapt";

    private static final String LOADING = "LOADING...";
    private static final String EXHAUSTED = "EXHAUSTED...";
    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    private RecipeListener onRecipeListener;
    private List<Recipe> recipes;


    public RecipeRecyclerViewAdapter(RecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
        recipes = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        switch (i) {
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lodaing, viewGroup, false);
                return new GeniricViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_exhausted, viewGroup, false);
                return new GeniricViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipes.get(i).getImage_url())
                    .into(((RecipeViewHolder) viewHolder).image);
            ((RecipeViewHolder) viewHolder).title.setText(recipes.get(i).getTitle());
            ((RecipeViewHolder) viewHolder).publisher.setText(recipes.get(i).getPublisher());
            ((RecipeViewHolder) viewHolder).rating.setText(String.valueOf(recipes.get(i).getSocial_rank()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipes.get(position).getTitle().equals(LOADING)) {
            return LOADING_TYPE;
        } else if (recipes.get(position).getTitle().equals(EXHAUSTED)) {
            return EXHAUSTED_TYPE;
        } else if (position == recipes.size() - 1 && position != 0 && !recipes.get(position).getTitle().equals(EXHAUSTED)) {
            return LOADING_TYPE;
        } else
            return RECIPE_TYPE;
    }

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING);
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    public void displayExhausted() {
        hideLoading();
        Recipe recipe = new Recipe();
        recipe.setTitle(EXHAUSTED);
        recipes.add(recipe);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if (isLoading()) {
            for (Recipe recipe : recipes) {
                if (recipe.getTitle().equals(LOADING))
                    recipes.remove(recipe);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if (recipes.size() > 0)
            if (recipes.get(recipes.size() - 1).getTitle().equals(LOADING))
                return true;
        return false;
    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size();
        return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView publisher;
        TextView rating;
        RecipeListener recipeListener;

        public RecipeViewHolder(@NonNull View itemView, RecipeListener recipeListener) {
            super(itemView);
            image = itemView.findViewById(R.id.recipe_image);
            title = itemView.findViewById(R.id.recipe_title);
            publisher = itemView.findViewById(R.id.recipe_publisher);
            rating = itemView.findViewById(R.id.reciper_rating);
            this.recipeListener = recipeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recipeListener.onRecipeClickListener(getAdapterPosition());
        }
    }

    public interface RecipeListener {
        void onRecipeClickListener(int index);
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if (recipes != null) {
            if (recipes.size() > 0) {
                return recipes.get(position);
            }
        }
        return null;
    }
}
