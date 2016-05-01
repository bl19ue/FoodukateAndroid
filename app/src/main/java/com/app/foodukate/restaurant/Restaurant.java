package com.app.foodukate.restaurant;

import com.google.gson.annotations.Expose;

/**
 * Created by pbansal on 4/25/16.
 */
public class Restaurant {
    public Restaurant(RestaurantBuilder restaurantBuilder){
        this.id = restaurantBuilder.id;
        this.name = restaurantBuilder.name;
        this.address = restaurantBuilder.address;
        this.location = restaurantBuilder.location;
        this.menu = restaurantBuilder.menu;
    }

    @Expose private String id;

    public String getId() {
        return _id;
    }

    public String getRestaurantid() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getMenu() {
        return menu;
    }

    @Expose private String _id;
    @Expose private String name;
    @Expose private String location;
    @Expose private String address;
    @Expose private String menu;

}
