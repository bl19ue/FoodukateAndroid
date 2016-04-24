package com.app.foodukate.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramyapatil on 4/17/16.
 */
public class UserSingleton {

    private static UserSingleton uInstance = null;
    private String name;
    private String email;
    private String picUrl;
    private String phoneNo;
    private List<String> interests = new ArrayList<>();

    public UserSingleton(){
    }

    public static UserSingleton getInstance(){
        if(uInstance == null) {
            uInstance = new UserSingleton();
        }
        return uInstance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
