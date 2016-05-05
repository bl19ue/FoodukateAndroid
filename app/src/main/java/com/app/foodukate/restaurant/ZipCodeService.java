package com.app.foodukate.restaurant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumitvalecha on 5/4/16.
 */
public class ZipCodeService {
    public static Object getService(Class<?> cls) {
        if(retrofit == null) {
            new ZipCodeService();
        }

        return retrofit.create(cls);
    }

    private final String api = "http://maps.googleapis.com";
    private static Retrofit retrofit;

    private ZipCodeService() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
