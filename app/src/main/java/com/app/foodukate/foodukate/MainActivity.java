package com.app.foodukate.foodukate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.app.foodukate.client.RestService;
import com.app.foodukate.recipe.RecipeApi;
import com.app.foodukate.recipe.RecipeListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String recipe_name = getIntent().getStringExtra("recipe_name");
        String searchBy = "";

        if(recipe_name != null) {
            searchBy = recipe_name;
        } else {
            searchBy = null;
        }

        searchRecipe(searchBy);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        String query = handleIntent(getIntent());

        if(query != null) {
            searchRecipe(query);
        }
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

    /**
     * For search by search box
     *
     * @param intent
     * @return query if searched by search box
     */
    private String handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            return query;
        }

        return null;
    }

    private void searchRecipe(String searchBy) {
        final RecipeApi recipeApi = (RecipeApi) RestService.getService(RecipeApi.class);
        if(searchBy == null) {
            attachFragment(null);
        } else {
            Call<ResponseBody> recipes = recipeApi.getRecipeByName(searchBy);

            recipes.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject recipesResponse = new JSONObject(response.body().string());
                        JSONArray recipes = recipesResponse.getJSONObject("recipes").getJSONArray("data");

                        Bundle bundle = new Bundle();
                        bundle.putString("recipes", recipes.toString());
                        attachFragment(bundle);
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: IOException: " + e.toString());
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: JSONException:  " + e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Enqueue: " + t.getMessage());
                }
            });
        }
    }

    private void attachFragment(Bundle bundle) {
        FragmentManager fragmentManager = getFragmentManager();
        if(currentFragment != null) {
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        }
        // Or: FragmentManager fragmentManager = getSupportFragmentManager()
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new RecipeListFragment();
        currentFragment = fragment;
        if(bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.add(R.id.recipe_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private static Fragment currentFragment = null;
    private static final String TAG = "MainActivity: ";
}
