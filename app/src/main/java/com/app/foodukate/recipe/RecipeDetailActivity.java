package com.app.foodukate.recipe;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.foodukate.client.RestService;
import com.app.foodukate.foodukate.BaseActivity;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.notification.FollowBody;
import com.app.foodukate.notification.UserCallApi;
import com.app.foodukate.user.UserSingleton;
import com.app.foodukate.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends BaseActivity {

     String recipeSource=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        this.recipeId = getIntent().getStringExtra("recipe_id");
        // Send GET request to get the recipe detail
        Log.i(TAG, "onCreate: recipe_id: " + this.recipeId);

        ImageButton shareBtn = (ImageButton) findViewById(R.id.menu_item_share);
        ImageButton followBtn = (ImageButton) findViewById(R.id.menu_item_follow);
        ImageButton followUserBtn = (ImageButton) findViewById(R.id.follow_user_button);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RecipeDetailActivity.this, "Share Btn Clicked", Toast.LENGTH_SHORT);
                UserSingleton loginUser = UserSingleton.getInstance();
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeDetailActivity.this, "Follow Btn Clicked", Toast.LENGTH_SHORT);
            }
        });

        followUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSingleton loginUser = UserSingleton.getInstance();
                final UserCallApi userApi = (UserCallApi) RestService.getService(UserCallApi.class);
                FollowBody fb = new FollowBody();
                fb.setLoggedInUsrEmail(loginUser.getEmail());
                fb.setOtherUsrEmail(recipeSource);
                Call<ResponseBody> response = userApi.followUser(fb);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                Log.e(TAG, response.body().string());
                            }
                            else{
                                Log.e(TAG,response.errorBody().toString());
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: IOException: " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "Enqueue: " + t.getMessage());
                    }
                });
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
                    recipeSource = recipeDetailData.getString("source");
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

        //TO DO

    }

    private String recipeId = "";
    private static final String TAG = "RecipeDetailActivity: ";
}
