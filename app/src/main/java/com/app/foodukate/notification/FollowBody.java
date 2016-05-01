package com.app.foodukate.notification;

/**
 * Created by sudhakarkamanboina on 4/24/16.
 */
public class FollowBody {

    private String loggedInUsrEmail;
    private String otherUsrEmail;


    public String getLoggedInUsrEmail() {
        return loggedInUsrEmail;
    }

    public void setLoggedInUsrEmail(String loggedInUsrEmail) {
        this.loggedInUsrEmail = loggedInUsrEmail;
    }

    public String getOtherUsrEmail() {
        return otherUsrEmail;
    }

    public void setOtherUsrEmail(String otherUsrEmail) {
        this.otherUsrEmail = otherUsrEmail;
    }
}
