package com.app.foodukate.recipe.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.foodukate.foodukate.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {


    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ListView ingredientsListView = (ListView) view.findViewById(R.id.ingredients_list);
        JSONObject recipeDetail = null;
        try {
            recipeDetail = new JSONObject(getArguments().getString("recipeDetail"));
            JSONArray data = recipeDetail.getJSONObject("recipe").getJSONArray("data");
            JSONArray ingredients = data.getJSONObject(0).getJSONArray("ingredients");

            String[] ingredientsList = new String[ingredients.length()];
            for(int i=0;i<ingredients.length();i++) {
                ingredientsList[i] = ingredients.getString(i);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, ingredientsList);

            ingredientsListView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }

        return view;
    }

    private static final String TAG = "IngredientsFragment: ";
}
