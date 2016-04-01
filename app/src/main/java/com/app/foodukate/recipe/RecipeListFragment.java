package com.app.foodukate.recipe;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.foodukate.client.RestService;
import com.app.foodukate.foodukate.R;

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
        View view =  inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ListView recipeList = (ListView) view.findViewById(R.id.recipe_list);

        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(getActivity(), getRecipe());

        recipeList.setAdapter(recipeListAdapter);

//        RecipeApi recipeApi = (RecipeApi) RestService.getService(RecipeApi.class);
//        recipeApi.getAllRecipes(new Callback<Recipe>() {
//            @Override
//            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Recipe> call, Throwable t) {
//
//            }
//        });

        return view;
    }


    public ArrayList<Recipe> getRecipe() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withName("Egg")
                .withImageUrl("http://si.wsj.net/public/resources/images/OD-BF661_FOODUS_P_20150312213714.jpg").build());

        recipes.add(new RecipeBuilder().withName("Hot Dog")
                .withImageUrl("http://cache-graphicslib.viator.com/graphicslib/thumbs674x446/5471/SITours/small-group-chicago-food-tour-wicker-park-and-bucktown-in-chicago-108824.jpg").build());

        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        recipes.add(new RecipeBuilder().withName("Sushi")
                .withImageUrl("http://www.akakiko.com.cy/wp-content/uploads/2015/05/sushi-set-small.jpg").build());

        return recipes;
    }

}
