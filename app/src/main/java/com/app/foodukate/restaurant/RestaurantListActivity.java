package com.app.foodukate.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


import com.app.foodukate.foodukate.BaseActivity;
import com.app.foodukate.foodukate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pbansal on 4/25/16.
 */
public class RestaurantListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        ListView restaurantListResults = (ListView) findViewById(R.id.restarant_location_list);
        String restaurants = null;
        try {
            restaurants = getIntent().getStringExtra("searchResults");
        } catch (Exception e) {
            Log.e(TAG, "onCreate:" + e.getMessage());
        }

        if (restaurants != null) {
            try {
                JSONArray restaurantJSONList = new JSONArray(restaurants);
                ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                for (int i = 0; i < restaurantJSONList.length(); i++) {
                    JSONObject restaurant = restaurantJSONList.getJSONObject(i);
                    String name = restaurant.getString("name");

                    Restaurant thisRestaurant = new RestaurantBuilder().withName(name).build();
                    restaurantArrayList.add(thisRestaurant);
                }
                RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurantArrayList);
                restaurantListResults.setAdapter(restaurantListAdapter);

            } catch (JSONException e) {
                Log.e(TAG, "onCreate: JSONException: " + e.getMessage());
            }

        } else {

        }


    }

    private static final String TAG = "RestaurantListActivity:";

}
