package com.app.foodukate.client;

import com.app.foodukate.common.Environment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumitvalecha on 3/14/16.
 */


public class RestService {

    public static Object getService(Class<?> cls) {
        if(retrofit == null) {
            new RestService();
        }

        return retrofit.create(cls);
    }

    private final String api = Environment.NODESERVICE;
    private static Retrofit retrofit;

    private RestService() {
        retrofit = new Retrofit.Builder().baseUrl(api).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
