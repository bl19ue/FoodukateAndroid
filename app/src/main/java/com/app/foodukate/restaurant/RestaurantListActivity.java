package com.app.foodukate.restaurant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;


import com.app.foodukate.client.RestService;
import com.app.foodukate.common.GoogleSingleton;
import com.app.foodukate.foodukate.BaseActivity;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.RecipeApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pbansal on 4/25/16.
 */
public class RestaurantListActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        zipcodeAvailable = false;

        restaurants = new ArrayList<>();

        recipeName = (EditText) findViewById(R.id.location_recipe_name);
        restaurantListResults = (ListView) findViewById(R.id.restarant_location_list);
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants);

        restaurantListResults.setAdapter(restaurantListAdapter);

        zipCodeApi = (ZipCodeApi) ZipCodeService.getService(ZipCodeApi.class);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        restaurantApi = (RestaurantApi) RestService.getService(RestaurantApi.class);

        recipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                restaurants.clear();
                if (zipcodeAvailable) {
                    Call<ResponseBody> restaurantResponse =
                            restaurantApi.getRestaurants(recipeName.getText().toString(), "94105"); //zipcode);

                    restaurantResponse.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject restaurantResponse = new JSONObject(response.body().string());
                                JSONArray dataArray = restaurantResponse.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    String _id = dataArray.getJSONObject(i).getString("_id");
                                    String name = dataArray.getJSONObject(i).getString("name");
                                    String locality = dataArray.getJSONObject(i).getString("locality");
                                    String address = dataArray.getJSONObject(i).getString("street_address");

                                    Restaurant restaurant = new RestaurantBuilder()
                                            .withRestaurantId(_id)
                                            .withName(name)
                                            .withLocation(locality)
                                            .withAddress(address)
                                            .build();

                                    restaurants.add(restaurant);
                                }

                                restaurantListAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.e(TAG, "handleRestaurantResponse: JSONException:" + e.getMessage());
                            } catch (IOException e) {
                                Log.e(TAG, "handleRestaurantResponse: IOException:" + e.getMessage());
                            } catch (Exception e) {
                                Log.e(TAG, "handleRestaurantResponse: Exception:" + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(TAG, "handleRestaurantResponse: onFailure:" + t.getMessage());
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(RestaurantListActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        } else {
            getLocation();
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, new com.google.android.gms.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                        }
                    });
        }
        if (mLastLocation != null) {
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            String latlng = String.valueOf(lat) + "," + String.valueOf(lng);
            Call<ResponseBody> zipCodeCall = zipCodeApi.getZipCode(latlng);
            zipCodeCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject zipCodeObject = new JSONObject(response.body().string());
                        JSONArray results = zipCodeObject.getJSONArray("results");
                        String address = results.getJSONObject(0).getString("formatted_address");

                        zipcode = parseZipCode(address);
                        zipcodeAvailable = true;
                    } catch (JSONException e) {
                        Log.e(TAG, "handleZipCodeResponse: JSONException:" + e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, "handleZipCodeResponse: IOException:" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "handleZipCodeResponse: onFailure:" + t.getMessage());
                }
            });
        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                int a = 2;
                a = a + a;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Parses the address and returns the zipcode
     *
     * @param address
     * @return zipcode
     */
    private String parseZipCode(String address) {
        String zipcode = "";
        int count = 0;
        for (int i = 0; i < address.length(); i++) {
            char c = address.charAt(i);
            if (Character.isDigit(c)) {
                count++;
                zipcode += c;
                if (count == 5) {
                    break;
                }
            } else {
                count = 0;
                zipcode = "";
            }
        }

        return zipcode;
    }


    private Location myLocation;
    private RestaurantApi restaurantApi;
    private String zipcode = "";
    private boolean zipcodeAvailable;
    private GoogleApiClient mGoogleApiClient;
    private EditText recipeName;
    private ZipCodeApi zipCodeApi;
    private LocationRequest mLocationRequest;
    private RestaurantListAdapter restaurantListAdapter;
    private ListView restaurantListResults;
    private ArrayList<Restaurant> restaurants;
    private static final String TAG = "RestaurantListActivity:";
}
