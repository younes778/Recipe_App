package com.example.recipe_app.adapters;

import android.content.Context;
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

import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecipeRecyclerViewAdapt";

    private RecipeListener onRecipeListener;
    private List<Recipe> recipes;


    public RecipeRecyclerViewAdapter(RecipeListener onRecipeListener ) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
        return new ViewHolder(view, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(recipes.get(i).getImage_url())
                .into(viewHolder.image);
        viewHolder.title.setText(recipes.get(i).getTitle());
        viewHolder.publisher.setText(recipes.get(i).getPublisher());
        viewHolder.rating.setText(String.valueOf(recipes.get(i).getSocial_rank()));
    }

    @Override
    public int getItemCount() {
        if (recipes!=null)
            return recipes.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView publisher;
        TextView rating;
        RecipeListener recipeListener;

        public ViewHolder(@NonNull View itemView, RecipeListener recipeListener) {
            super(itemView);
            image = itemView.findViewById(R.id.recipe_image);
            title = itemView.findViewById(R.id.recipe_title);
            publisher = itemView.findViewById(R.id.recipe_publisher);
            rating = itemView.findViewById(R.id.reciper_rating);
            this.recipeListener = recipeListener;
        }

        @Override
        public void onClick(View v) {
            recipeListener.onRecipeClickListener(getAdapterPosition());
        }
    }

    public interface RecipeListener {
        public void onRecipeClickListener(int index);
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
