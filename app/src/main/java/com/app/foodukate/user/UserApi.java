package com.app.foodukate.user;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ramyapatil on 4/20/16.
 */
public interface UserApi {
    @POST("/users/register")
    public Call<ResponseBody> createUser(@Body User body);

    @GET("/users/authorize/{email}")
    public Call<ResponseBody> authorizeUser(@Path("email") String email);
//
//    @GET("recipes/search/name/{name}")
//    public Call<ResponseBody> getRecipeByName(@Path("name") String name);
//
//    @GET("recipes/search/id/{recipeId}")
//    public Call<ResponseBody> getRecipeById(@Path("recipeId") String recipeId);
}
