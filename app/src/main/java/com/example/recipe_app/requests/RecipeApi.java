package com.example.recipe_app.requests;

import com.example.recipe_app.requests.responses.RecipeResponse;
import com.example.recipe_app.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //Search recipe request
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
      @Query("key") String key,
      @Query("q") String query,
      @Query("page") String page
    );

    //Get recipe request
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );
}
