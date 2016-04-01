package com.app.foodukate.recipe;

import com.app.foodukate.recipe.Recipe;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sumitvalecha on 3/14/16.
 */
public interface RecipeApi {
    @GET("/recipe")
    public void getAllRecipes(Callback<Recipe> response);
}
