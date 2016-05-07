package com.app.foodukate.recipe.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.foodukate.recipe.RecipeDetailParser;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.Recipe;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {


    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        JSONObject recipeDetail = null;
//        ListView recipeDescriptionListView = (ListView) view.findViewById(R.id.recipe_category_list);
        TextView recipeStepsLabelTextView = (TextView) view.findViewById(R.id.recipe_steps_label);
        TextView recipeStepsTextView = (TextView) view.findViewById(R.id.recipe_steps);
        TextView recipeSourceURLTextView = (TextView) view.findViewById(R.id.recipe_source_url);
        TextView recipeCuisineTextView = (TextView) view.findViewById(R.id.recipe_cuisine);
        TextView recipeCategoryTextView = (TextView) view.findViewById(R.id.recipe_category);
        TextView recipeCourseTextView = (TextView) view.findViewById(R.id.recipe_course);
        TextView recipeCookingTimeTextView = (TextView) view.findViewById(R.id.recipe_cooking_time);
        try {
            recipeDetail = new JSONObject(getArguments().getString("recipeDetail"));
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }
        if (recipeDetail != null) {
            try {
                Recipe recipe = RecipeDetailParser.parseRecipeDetail(recipeDetail);
                if (recipe!=null){
                    String sourceURL = recipe.getSourceUrl();
                    String cookingTime = recipe.getCookingTime();

                    if(sourceURL!=null && !sourceURL.equals("")){
                        recipeSourceURLTextView.append(sourceURL);
                        recipeSourceURLTextView.setVisibility(View.VISIBLE);
                    }
                    if(cookingTime!=null && !cookingTime.equals("")){
                        recipeCookingTimeTextView.append(cookingTime + " mins");
                        recipeCookingTimeTextView.setVisibility(View.VISIBLE);
                    }
                    displayArrayInTextView(recipe.getCategories(), recipeCategoryTextView);
                    displayArrayInTextView(recipe.getCuisines(), recipeCuisineTextView);
                    displayArrayInTextView(recipe.getCourses(), recipeCourseTextView);
                    displayArrayInTextView(recipe.getSteps(), recipeStepsTextView);
                }
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: " + e.getMessage());
            }
        }

        return view;
    }

    private void displayArrayInTextView(ArrayList<String> arrayList,
                                            TextView textView) {
        int length = arrayList.size();
        for (int i = 0; i < length; i++) {
            try {
                String value = arrayList.get(i);
                if (i == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.append(value);
                } else {
                    textView.append(", " + value);
                }
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: " + e.getMessage());
            }
        }
    }

    private static final String TAG = "RecipeFragment: ";

}
