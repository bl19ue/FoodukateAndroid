package com.app.foodukate.menu_scanner;

import com.app.foodukate.common.Environment;

import java.io.File;

import okhttp3.MultipartBody;

/**
 * Created by sumitvalecha on 4/10/16.
 */
public class OCRBody {
    public OCRBody(File imageFile) {
        this.file = imageFile;
    }

    public File getImageFile() {
        return file;
    }

    public String getApikey() {
        return apikey;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isOverlayRequired() {
        return isOverlayRequired;
    }

    private File file;
    private String apikey = Environment.APIKEY;
    private String language = Environment.LANGUAGE;
    private boolean isOverlayRequired = false;
}
