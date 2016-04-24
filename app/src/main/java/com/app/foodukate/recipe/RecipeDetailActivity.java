package com.app.foodukate.recipe;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.foodukate.client.RestService;
import com.app.foodukate.foodukate.BaseActivity;
import com.app.foodukate.foodukate.MainActivity;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        this.recipeId = getIntent().getStringExtra("recipe_id");
        // Send GET request to get the recipe detail
        Log.i(TAG, "onCreate: recipe_id: " + this.recipeId);

        Button login = (Button) findViewById(R.id.follow_user_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        final RecipeApi recipeApi = (RecipeApi) RestService.getService(RecipeApi.class);
        Call<ResponseBody> responseBodyCall = recipeApi.getRecipeById(recipeId);

        handleResponse(responseBodyCall);
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

    private void handleResponse(Call<ResponseBody> responseBodyCall) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Bundle bundle = new Bundle();
                    JSONObject recipeObject = new JSONObject(response.body().string());
                    JSONObject recipeDetailData = recipeObject.getJSONObject("recipe").getJSONObject("data");
                    loadImageandText(recipeDetailData);
                    loadFollowStar();

                    bundle.putString("recipeDetail", recipeDetailData.toString());
                    RecipeDetailPagerAdapter recipeDetailPagerAdapter =
                            new RecipeDetailPagerAdapter(getSupportFragmentManager(), bundle);

                    ViewPager viewPager = (ViewPager) findViewById(R.id.recipe_detail_swipe);
                    viewPager.setAdapter(recipeDetailPagerAdapter);

                } catch (IOException e) {
                    Log.e(TAG, "handleResponse: IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.e(TAG, "handleResponse: JSONException: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void loadImageandText(JSONObject recipeDetail){
        final NetworkImageView recipeDetailImage = (NetworkImageView) this.findViewById(R.id.recipe_detail_image);
        final TextView recipeDetailName = (TextView) this.findViewById(R.id.recipe_detail_name);
        final TextView recipeDetailSource = (TextView) this.findViewById(R.id.recipe_detail_source);
        final TextView recipeDetailRating = (TextView) this.findViewById(R.id.recipe_detail_rating);

        try{
            ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
            recipeDetailImage.setImageUrl(recipeDetail.getString("imgUrl"), imageLoader);
            recipeDetailName.setText(recipeDetail.getString("name"));
            recipeDetailRating.setText(recipeDetail.getString("rating"));
            recipeDetailSource.setText("by " + recipeDetail.getJSONObject("source").getString("sourceDisplayName"));

        }catch (JSONException e) {
            Log.e(TAG, "handleResponse: JSONException: " + e.getMessage());
        }

    }

    private void loadFollowStar(){

    }

    private String recipeId = "";
    private static final String TAG = "RecipeDetailActivity: ";
}
