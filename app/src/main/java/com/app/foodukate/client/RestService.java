package com.app.foodukate.client;

import retrofit2.Retrofit;

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

    private final String api = "http://IP:PORT";
    private static Retrofit retrofit;

    private RestService() {
        retrofit = new Retrofit.Builder().baseUrl(api).build();
    }
}
