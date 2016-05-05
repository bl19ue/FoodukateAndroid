package com.app.foodukate.restaurant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sumitvalecha on 5/4/16.
 */
public interface RestaurantApi {
    @GET("restaurant/search/{recipeName}/{zipcode}")
    public Call<ResponseBody> getRestaurants(@Path("recipeName") String recipeName, @Path("zipcode") String zipcode);
}
