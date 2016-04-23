package com.app.foodukate.notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Created by sudhakarkamanboina on 4/19/16.
 */
public interface UserCallApi {

    @PUT("users/")
    public Call<ResponseBody> updateUser(@Body GcmBody body);
}
