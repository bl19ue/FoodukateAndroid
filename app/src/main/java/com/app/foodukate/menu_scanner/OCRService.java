package com.app.foodukate.menu_scanner;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumitvalecha on 4/11/16.
 */
public class OCRService {

    public static Object getService(Class<?> cls) {
        if(retrofit == null) {
            new OCRService();
        }

        return retrofit.create(cls);
    }

    private final String api = "https://api.ocr.space";
    private static Retrofit retrofit;

    private OCRService() {
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
