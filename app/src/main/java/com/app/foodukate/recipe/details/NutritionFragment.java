package com.app.foodukate.recipe.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.foodukate.foodukate.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NutritionFragment extends Fragment {


    public NutritionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        ListView nutritionListView = (ListView) view.findViewById(R.id.nutritions_list);
        JSONObject recipeDetail = null;
        try{
            recipeDetail = new JSONObject(getArguments().getString("recipeDetail"));
        }catch (Exception e){
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }
        if(recipeDetail!=null){
            try{
                JSONObject nutritionJSONObject = recipeDetail.getJSONObject("nutritionValues");
                Map<String, String> nutritionMap = new HashMap<>();

                Iterator<String> keys = nutritionJSONObject.keys();
                while (keys.hasNext()){
                    String key = (String) keys.next();
                    String value = (String) nutritionJSONObject.getString(key);
                    nutritionMap.put(key,value);
                }

                NutritionAdapter nutritionAdapter = new NutritionAdapter(getActivity(), nutritionMap);
                nutritionListView.setAdapter(nutritionAdapter);

            }catch (Exception e){
                Log.e(TAG, "onCreateView: JSONException: " + e.getMessage());
            }
        }

        return  view;
    }

    private static final String TAG = "NutritionsFragment: ";

}
