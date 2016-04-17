package com.app.foodukate.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.app.foodukate.recipe.details.IngredientsFragment;
import com.app.foodukate.recipe.details.NutritionFragment;
import com.app.foodukate.recipe.details.RecipeFragment;

/**
 * Created by sumitvalecha on 3/25/16.
 */
public class RecipeDetailPagerAdapter extends FragmentPagerAdapter {
    public RecipeDetailPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0: {
                fragment = new IngredientsFragment();
                break;
            }
            case 1: {
                fragment = new NutritionFragment();
                break;
            }
            case 2: {
                fragment = new RecipeFragment();
                break;
            }
        }

        try {
            fragment.setArguments(this.bundle);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    private final Bundle bundle;
    private static final String TAG = "RecipePagerAdapter: ";
}
