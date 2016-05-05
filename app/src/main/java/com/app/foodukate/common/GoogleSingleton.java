package com.app.foodukate.common;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by sumitvalecha on 5/4/16.
 */
public class GoogleSingleton {
    public GoogleSingleton(GoogleApiClient mGoogleApiClientInst) {
        mGoogleApiClient = mGoogleApiClientInst;
    }

    public static GoogleApiClient getInstance() {
        return mGoogleApiClient;
    }

    private static GoogleApiClient mGoogleApiClient;
}
