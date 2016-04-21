package com.app.foodukate.recipe;

import java.util.ArrayList;

/**
 * Created by sumitvalecha on 3/14/16.
 */
public class RecipeBuilder {

    String id;
    String _id;
    String name;
    String source;
    String rating;
    String imageUrl;
    String cookingTime;
    String thumbnailUrl;
    String miscellaneous;

    ArrayList<String> steps;
    ArrayList<String> alias;
    ArrayList<String> courses;
    ArrayList<String> cuisines;
    ArrayList<String> categories;
//  TODO  ArrayList<String> ingredients;

    public Recipe build() {
        return new Recipe(this);
    }

    public RecipeBuilder withId(String _id) {
        this._id = _id;
        return this;
    }

    public RecipeBuilder withRecipeId(String id) {
        this.id = id;
        return this;
    }

    public RecipeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RecipeBuilder withSource(String source) {
        this.source = source;
        return this;
    }

    public RecipeBuilder withRating(String rating) {
        this.rating = rating;
        return this;
    }

    public RecipeBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public RecipeBuilder withCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeBuilder withThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public RecipeBuilder withMiscellaneous(String miscellaneous) {
        this.miscellaneous = miscellaneous;
        return this;
    }

    public RecipeBuilder withSteps(ArrayList<String> steps) {
        this.steps = steps;
        return this;
    }

    public RecipeBuilder withAlias(ArrayList<String> alias) {
        this.alias = alias;
        return this;
    }

    public RecipeBuilder withCourses(ArrayList<String> courses) {
        this.courses = courses;
        return this;
    }

    public RecipeBuilder withCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
        return this;
    }

    public RecipeBuilder withCategories(ArrayList<String> categories) {
        this.categories = categories;
        return this;
    }

}
