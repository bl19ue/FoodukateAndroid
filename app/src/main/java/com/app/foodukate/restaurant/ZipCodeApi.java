package com.app.foodukate.restaurant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sumitvalecha on 5/4/16.
 */
public interface ZipCodeApi {
    @GET("/maps/api/geocode/json")
    public Call<ResponseBody> getZipCode(@Query("latlng") String latlng);
}
