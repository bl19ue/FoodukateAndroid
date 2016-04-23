package com.app.foodukate.user;

/**
 * Created by ramyapatil on 4/20/16.
 */
public class User {
    private String name;
    private String email;
    private String imgUrl;
    private String phoneNo;
    private String[] interests;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String[] getInterest() {
        return interests;
    }

    public void setInterest(String[] interest) {
        this.interests = interest;
    }
}

//    name: String,
//    email: String,
//    imgUrl: String,
//    phoneNo: String,
//    interest: [String],