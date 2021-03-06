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
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        TextView ingredientServings = (TextView) view.findViewById(R.id.ingredients_servings);
        ListView ingredientsListView = (ListView) view.findViewById(R.id.ingredients_list);
        JSONObject recipeDetail = null;
        try {
            recipeDetail = new JSONObject(getArguments().getString("recipeDetail"));
        }catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }
        if(recipeDetail!=null){
            try {
                JSONArray ingredientsJSONList = recipeDetail.getJSONArray("ingredients");
                List<String> ingredientsArrayList = new ArrayList<>();
                String numberOfServings = recipeDetail.getString("numberOfServings");
                ingredientServings.append(numberOfServings);

                for(int i=0;i<ingredientsJSONList.length();i++) {
                    ingredientsArrayList.add(ingredientsJSONList.getString(i));
                }

                IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity(), ingredientsArrayList);

                ingredientsListView.setAdapter(ingredientsAdapter);

            } catch (Exception e) {
                Log.e(TAG, "onCreateView: JSONException" + e.getMessage());
            }

        }else{

        }
        return view;
    }

    private static final String TAG = "IngredientsFragment: ";
}
