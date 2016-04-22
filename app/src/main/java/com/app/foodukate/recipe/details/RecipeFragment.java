package com.app.foodukate.recipe.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
        try {
            recipeDetail = new JSONObject(getArguments().getString("recipeDetail"));
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }
        if (recipeDetail != null) {
            try {
                String recipeSourceURL = recipeDetail.getJSONObject("source").getString("sourceRecipeUrl");
                JSONArray categories = recipeDetail.getJSONArray("categories");
                JSONArray cuisines = recipeDetail.getJSONArray("cuisines");
                JSONArray courses = recipeDetail.getJSONArray("course");
                JSONArray recipeSteps = recipeDetail.getJSONArray("steps");
                recipeSourceURLTextView.append(recipeSourceURL);
                displayJSONArrayInTextView(categories, recipeCategoryTextView);
                displayJSONArrayInTextView(cuisines, recipeCuisineTextView);
                displayJSONArrayInTextView(courses, recipeCourseTextView);
                displayJSONArrayInTextView(recipeSteps, recipeStepsTextView);

            } catch (Exception e) {
                Log.e(TAG, "onCreateView: JSONException: " + e.getMessage());
            }
        }

        return view;
    }

    private void displayJSONArrayInTextView(JSONArray jsonArrayToBeConverted,
                                            TextView textView) {
        int length = jsonArrayToBeConverted.length();

        for (int i = 0; i < length; i++) {
            try {
                String value = jsonArrayToBeConverted.getString(i);
                if (i == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.append(value);
                } else {
                    textView.append(", " + value);
                }
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: JSONConversion: " + e.getMessage());
            }
        }
    }

    private static final String TAG = "RecipeFragment: ";

}
