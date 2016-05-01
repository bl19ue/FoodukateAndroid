package com.app.foodukate.restaurant;

/**
 * Created by pbansal on 4/25/16.
 */
public class RestaurantBuilder {
    String id;
    String _id;
    String name;
    String location;
    String address;
    String menu;

    public Restaurant build(){
        return new Restaurant(this);
    }

    public RestaurantBuilder withId(String _id){
        this._id = _id;
        return this;
    }

    public RestaurantBuilder withRestaurantId(String id){
        this.id = id;
        return this;
    }

    public RestaurantBuilder withName(String name){
        this.name = name;
        return this;
    }

    public RestaurantBuilder withLocation(String location){
        this.location = location;
        return this;
    }

    public RestaurantBuilder withAddress(String address){
        this.address = address;
        return this;
    }

    public RestaurantBuilder menu(String menu){
        this.menu = menu;
        return this;
    }
}
