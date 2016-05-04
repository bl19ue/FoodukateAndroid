package com.app.foodukate.notification;

/**
 * Created by sudhakarkamanboina on 5/3/16.
 */
public class ShareBody {

    private String loggedInUsrEmail;
    private String recipeId;

    public String getLoggedInUsrEmail() {
        return loggedInUsrEmail;
    }

    public void setLoggedInUsrEmail(String loggedInUsrEmail) {
        this.loggedInUsrEmail = loggedInUsrEmail;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
