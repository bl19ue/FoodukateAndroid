package com.app.foodukate.foodukate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.app.foodukate.client.RestService;
import com.app.foodukate.gcm.QuickstartPreferences;
import com.app.foodukate.gcm.RegistrationIntentService;
import com.app.foodukate.recipe.RecipeApi;
import com.app.foodukate.recipe.RecipeListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        registerReceiver();
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
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

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
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
        // Or: FragmentManager fragmentManager = getSupportFragmentManager()
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeListFragment fragment = new RecipeListFragment();

        if(bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.add(R.id.recipe_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private static final String TAG = "MainActivity: ";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;

}
