package com.app.foodukate.recipe;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;
import com.app.foodukate.client.RestService;
import com.app.foodukate.common.Constant;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {


    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ListView recipeList = (ListView) view.findViewById(R.id.recipe_list);
        String recipes = null;

        try {
            recipes = getArguments().getString("recipes");
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }

        if (recipes != null) {
            try {
                JSONArray recipeJSONList = new JSONArray(recipes);
                ArrayList<Recipe> recipeArrayList = new ArrayList<>();


                for (int i = 0; i < recipeJSONList.length(); i++) {
                    String name, rating, source, imgUrl, id;
                    JSONObject recipe = recipeJSONList.getJSONObject(i);

                    name = recipe.getString("name");
                    try{
                        imgUrl = recipe.getString("imgUrl");
                        if (imgUrl.equals("") || imgUrl == null){
                            imgUrl = Constant.NO_IMG_URL;
                        }
                    }
                    catch (JSONException e){
                        imgUrl = Constant.NO_IMG_URL;
                    }
                    id = recipe.getString("id");
                    rating = recipe.getString("rating");
                    try{
                        source = "by " + recipe.getJSONObject("source").getString("sourceDisplayName");
                    }catch (JSONException e){
                        source = "by " + recipe.getString("source");
                    }

                    Recipe thisRecipe = new RecipeBuilder().withRecipeId(id)
                            .withName((!name.equals("") && name != null) ? name : "No name")
                            .withImageUrl(imgUrl)
                            .withRating((!rating.equals("") && rating != null) ? rating : "Not rated yet")
                            .withSource(source)
                            .build();
                    recipeArrayList.add(thisRecipe);
                }
                RecipeListAdapter recipeListAdapter = new RecipeListAdapter(getActivity(), recipeArrayList);
                recipeList.setAdapter(recipeListAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreateView: JSONException: " + e.getMessage());
            }
        } else {
            RecipeListAdapter recipeListAdapter = new RecipeListAdapter(getActivity(), getRecipe());

            recipeList.setAdapter(recipeListAdapter);

        }
        return view;
    }


    public ArrayList<Recipe> getRecipe() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new RecipeBuilder().withId("1234").withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Egg")
                .withImageUrl("http://si.wsj.net/public/resources/images/OD-BF661_FOODUS_P_20150312213714.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Hot Dog")
                .withImageUrl("http://cache-graphicslib.viator.com/graphicslib/thumbs674x446/5471/SITours/small-group-chicago-food-tour-wicker-park-and-bucktown-in-chicago-108824.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Egg")
                .withImageUrl("http://si.wsj.net/public/resources/images/OD-BF661_FOODUS_P_20150312213714.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withId("1234").withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        return recipes;
    }

    private static final String TAG = "RecipeListFragment: ";

}
