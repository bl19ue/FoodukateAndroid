package com.app.foodukate.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.foodukate.recipe.ingredients.Ingredient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sumitvalecha on 3/14/16.
 */

public class Recipe {

    public Recipe(RecipeBuilder recipeBuilder) {
        this.id = recipeBuilder.id;
        this.name = recipeBuilder.name;
        this.source = recipeBuilder.source;
        this.rating = recipeBuilder.rating;
        this.imageUrl = recipeBuilder.imageUrl;
        this.cookingTime = recipeBuilder.cookingTime;
        this.sourceUrl = recipeBuilder.sourceUrl;
        this.thumbnailUrl = recipeBuilder.thumbnailUrl;
        this.miscellaneous = recipeBuilder.miscellaneous;
        this.servings = recipeBuilder.servings;

        this.steps = recipeBuilder.steps;
        this.alias = recipeBuilder.alias;
        this.courses = recipeBuilder.courses;
        this.cuisines = recipeBuilder.cuisines;
        this.categories = recipeBuilder.categories;
        this.ingredients = recipeBuilder.ingredients;
    }

    public String getId() { return _id; }

    public String getRecipeId() { return id; }

    public String getName() { return name; }

    public String getSource() { return source; }

    public String getSourceUrl() { return sourceUrl; }

    public String getRating() { return rating; }

    public String getImageUrl() { return imageUrl; }

    public String getCookingTime() { return cookingTime; }

    public String getThumbnailUrl() { return thumbnailUrl; }

    public String getMiscellaneous() { return miscellaneous; }

    public String getServings() { return servings; }

    public ArrayList<String> getSteps() { return steps; }

    public ArrayList<String> getAlias() { return alias; }

    public ArrayList<String> getCourses() { return courses; }

    public ArrayList<String> getCuisines() { return cuisines; }

    public ArrayList<String> getCategories() { return categories; }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    @Expose @SerializedName("id") private String id;
    @Expose private String _id;
    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("source") private String source;
    private String sourceUrl;
    @Expose @SerializedName("rating") private String rating;
    @Expose @SerializedName("imgUrl") private String imageUrl;
    @Expose @SerializedName("cookingTime") private String cookingTime;
    @Expose @SerializedName("numberOfServings") private String servings;
    @Expose @SerializedName("thumbnailURL") private String thumbnailUrl;
    @Expose @SerializedName("miscellaneous") private String miscellaneous;

    @Expose @SerializedName("steps") private ArrayList<String> steps;
    @Expose @SerializedName("alias") private ArrayList<String> alias;
    @Expose @SerializedName("course") private ArrayList<String> courses;
    @Expose @SerializedName("cuisines") private ArrayList<String> cuisines;
    @Expose @SerializedName("categories") private ArrayList<String> categories;

    @Expose @SerializedName("ingredients") private ArrayList<Ingredient> ingredients;

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
