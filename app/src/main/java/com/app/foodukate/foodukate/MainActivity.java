package com.app.foodukate.foodukate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.app.foodukate.recipe.RecipeListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String query = handleIntent(getIntent());

        FragmentManager fragmentManager = getFragmentManager();
        // Or: FragmentManager fragmentManager = getSupportFragmentManager()
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeListFragment fragment = new RecipeListFragment();

        Bundle bundle = new Bundle();
        if(query == null) {
            String recipes = getIntent().getStringExtra("recipes");
            if (savedInstanceState == null) {
                bundle.putString("recipes", recipes);
            }
        } else {
            bundle.putString("recipeQuery", query);
        }

        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.recipe_fragment_container, fragment);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            return query;
        }

        return null;
    }

}
