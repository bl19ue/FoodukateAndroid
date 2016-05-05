package com.app.foodukate.recipe;

import com.app.foodukate.recipe.Recipe;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sumitvalecha on 3/14/16.
 */
public interface RecipeApi {
    @GET("recipes/")
    public void getAllRecipes(Callback<Recipe> response);

    @GET("recipes/recommended/email/{email}")
    public Call<ResponseBody> getRecommendedList(@Path("email") String email);

    @GET("recipes/search/name/{name}")
    public Call<ResponseBody> getRecipeByName(@Path("name") String name);

    @GET("recipes/search/id/{recipeId}")
    public Call<ResponseBody> getRecipeById(@Path("recipeId") String recipeId);

    @POST("recipes/create")
    public Call<ResponseBody> addRecipe(@Body Recipe recipe);

    @GET("ingredients/search/{name}")
    public Call<ResponseBody> getIngredientsByName(@Path("name") String name);
}
