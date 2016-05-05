package com.app.foodukate.user;

/**
 * Created by ramyapatil on 4/20/16.
 */
public class User {
    private String name;
    private String email;
    private String imgurl;
    private String phoneno;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPhoneNo() {
        return phoneno;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneno = phoneNo;
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