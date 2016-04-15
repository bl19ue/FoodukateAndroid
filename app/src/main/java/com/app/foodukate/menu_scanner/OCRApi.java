package com.app.foodukate.menu_scanner;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sumitvalecha on 4/10/16.
 */
public interface OCRApi {
    @Multipart
    @POST("/parse/image")
    Call<ResponseBody> getScannedResults(@Part("apikey") RequestBody apikey,
                              @Part("language") RequestBody language,
                              @Part("isOverlayRequired") boolean isOverlayRequired,
                              @Part MultipartBody.Part file);
}
