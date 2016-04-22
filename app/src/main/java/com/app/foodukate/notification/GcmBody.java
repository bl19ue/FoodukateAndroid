package com.app.foodukate.notification;

/**
 * Created by sudhakarkamanboina on 4/19/16.
 */
public class GcmBody {
    private String userId;
    private String gcmToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
