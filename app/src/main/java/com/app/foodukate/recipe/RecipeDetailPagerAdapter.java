package com.app.foodukate.recipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.foodukate.recipe.details.IngredientsFragment;
import com.app.foodukate.recipe.details.NutritionFragment;
import com.app.foodukate.recipe.details.RecipeFragment;

/**
 * Created by sumitvalecha on 3/25/16.
 */
public class RecipeDetailPagerAdapter extends FragmentPagerAdapter {
    public RecipeDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new IngredientsFragment();
            case 1: return new NutritionFragment();
            case 2: return new RecipeFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
