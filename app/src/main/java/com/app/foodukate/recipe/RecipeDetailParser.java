package com.app.foodukate.recipe;

import android.util.Log;

import com.app.foodukate.common.Constant;
import com.app.foodukate.recipe.Recipe;
import com.app.foodukate.recipe.RecipeBuilder;
import com.app.foodukate.user.UserSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pbansal on 5/7/16.
 */
public class RecipeDetailParser {
    public static Recipe parseRecipeDetail(JSONObject recipeDetail){
        Recipe responseRecipe = null;
        String name, rating, source, imgUrl, cookingTime, servings, sourceUrl;
        ArrayList<String> categories, cuisines, courses, recipeSteps;
        try {
            name = recipeDetail.getString("name");
            rating = recipeDetail.getString("rating");
            cookingTime = recipeDetail.getString("cookingTime");
            servings = recipeDetail.getString("numberOfServings");
            if (name.equals("") || name == null) {
                name = "No name";
            }
            if(rating.equals("") || rating == null) {
                rating ="Not rated yet";
            }
            if (cookingTime.equals("") || cookingTime == null) {
                name = "Not specified";
            }
            if (servings.equals("") || servings == null) {
                servings = "Not specified";
            }
            Object obj = recipeDetail.get("source");
            if(obj instanceof JSONObject){
                source = "by " + recipeDetail.getJSONObject("source").getString("sourceDisplayName");
                sourceUrl = ((JSONObject) obj).getString("sourceRecipeUrl");
            }else {
                source = "by " + recipeDetail.getString("source");
                sourceUrl = null;
            }
            obj = recipeDetail.get("imgUrl");
            if(obj instanceof String){
                imgUrl = recipeDetail.getString("imgUrl");
                if (imgUrl.equals("") || imgUrl == null){
                    imgUrl = Constant.NO_IMG_URL;
                }
            }else {
                imgUrl = Constant.NO_IMG_URL;
            }
            obj = recipeDetail.get("categories");
            if(obj instanceof JSONArray){
                categories = convertJSONArrayToArrayList(recipeDetail.getJSONArray("categories"));
            }else {
                categories = null;
            }
            obj = recipeDetail.getJSONArray("cuisines");
            if(obj instanceof JSONArray){
                cuisines = convertJSONArrayToArrayList(recipeDetail.getJSONArray("cuisines"));
            }else {
                cuisines = null;
            }
            obj = recipeDetail.getJSONArray("course");
            if(obj instanceof JSONArray){
                courses = convertJSONArrayToArrayList(recipeDetail.getJSONArray("course"));
            }else {
                courses = null;
            }
            obj = recipeDetail.getJSONArray("steps");
            if(obj instanceof JSONArray){
                recipeSteps = convertJSONArrayToArrayList(recipeDetail.getJSONArray("steps"));
            }else {
                recipeSteps = null;
            }

            RecipeBuilder recipeBuilder = new RecipeBuilder()
                    .withName(name)
                    .withRating(rating)
                    .withCourses(courses)
                    .withCategories(categories)
                    .withServings(servings)
                    .withCuisines(cuisines)
                    .withSource(source)
                    .withSourceUrl(sourceUrl)
                    .withCookingTime(cookingTime)
                    .withImageUrl(imgUrl)
                    .withSteps(recipeSteps);
            responseRecipe = recipeBuilder.build();

        }catch (Exception e){
            Log.e(TAG, "JSONException: " + e.getMessage());
        }

        return responseRecipe;
    }

    private static ArrayList<String> convertJSONArrayToArrayList(JSONArray jsonArray){
        ArrayList<String> responseArrayList = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                responseArrayList.add(jsonArray.get(i).toString());
            }
        }catch (Exception e){
            Log.e(TAG, "JSONException: " + e.getMessage());

        }

        return responseArrayList;
    }

    private static final String TAG = "Recipe_Detail_Parser";
}
