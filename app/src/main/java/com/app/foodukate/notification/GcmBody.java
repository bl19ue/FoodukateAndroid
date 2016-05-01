package com.app.foodukate.notification;

/**
 * Created by sudhakarkamanboina on 4/19/16.
 */
public class GcmBody {
    private String userEmail;
    private String gcmToken;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
