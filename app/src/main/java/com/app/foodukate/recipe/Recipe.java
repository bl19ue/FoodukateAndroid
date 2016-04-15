package com.app.foodukate.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by sumitvalecha on 3/14/16.
 */

public class Recipe {

    public Recipe(RecipeBuilder recipeBuilder) {
        this.id = recipeBuilder.id;
        this.name = recipeBuilder.name;
        this.source = recipeBuilder.source;
        this.imageUrl = recipeBuilder.imageUrl;
        this.cookingTime = recipeBuilder.cookingTime;
        this.thumbnailUrl = recipeBuilder.thumbnailUrl;
        this.miscellaneous = recipeBuilder.miscellaneous;

        this.steps = recipeBuilder.steps;
        this.alias = recipeBuilder.alias;
        this.courses = recipeBuilder.courses;
        this.cuisines = recipeBuilder.cuisines;
        this.categories = recipeBuilder.categories;
    }

    public String getId() { return _id; }

    public String getRecipeId() { return id; }

    public String getName() { return name; }

    public String getSource() { return source; }

    public String getImageUrl() { return imageUrl; }

    public String getCookingTime() { return cookingTime; }

    public String getThumbnailUrl() { return thumbnailUrl; }

    public String getMiscellaneous() { return miscellaneous; }


    public ArrayList<String> getSteps() { return steps; }

    public ArrayList<String> getAlias() { return alias; }

    public ArrayList<String> getCourses() { return courses; }

    public ArrayList<String> getCuisines() { return cuisines; }

    public ArrayList<String> getCategories() { return categories; }

    @Expose private String id;
    @Expose private String _id;
    @Expose private String name;
    @Expose private String source;
    @Expose private String imageUrl;
    @Expose private String cookingTime;
    @Expose private String thumbnailUrl;
    @Expose private String miscellaneous;

    @Expose private ArrayList<String> steps;
    @Expose private ArrayList<String> alias;
    @Expose private ArrayList<String> courses;
    @Expose private ArrayList<String> cuisines;
    @Expose private ArrayList<String> categories;

//  TODO  @Expose private ArrayList<String> ingredients;

}

//    name: String,
//    alias: [String],
//    imgUrl: Object,
//    thumbnailURL: String,
//    course: [String],
//    miscellaneous: String,
//    cuisines: [String],
//    categories: [String],
//    steps: [String],
//    ingredients: [{}],
//    source: Object,
//    cookingTime: Number
