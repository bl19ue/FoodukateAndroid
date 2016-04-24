package com.app.foodukate.foodukate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Response;
import retrofit2.Call;

import com.app.foodukate.client.RestService;
import com.app.foodukate.user.User;
import com.app.foodukate.user.UserApi;
import com.app.foodukate.user.UserSingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by ramyapatil on 4/20/16.
 */
public class RegistrationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    private static final int SIGN_IN_CODE = 9001;
    ListView listview;
    Context context;
    private Button continueButton;
    UserSingleton loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loggedInUser = UserSingleton.getInstance();

        ImageView profilepic = (ImageView)findViewById(R.id.picture);
        String personPhotoUrl = loggedInUser.getPicUrl();

        new LoadProfileImage(profilepic).execute(personPhotoUrl);
        TextView name = (TextView)findViewById(R.id.username);
        name.setText(loggedInUser.getName());
        TextView email = (TextView)findViewById(R.id.emailAddr);
        email.setText(loggedInUser.getEmail());
        listview = (ListView)findViewById(R.id.listView);

        String[] cuisine = {"Asian","Barbecue","Greek","Mediterranean","Indian","Kid-Friendly",
                "Thai","Mexican","Spanish","Japanese","American","Chinese","Italian",
                "Southwestern","Moroccan","Cuban","English","French","Hungarian","German",
                "Swedish","Hawaiian","Cajun & Creole","Portuguese","Irish","Southern & Soul Food"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.cuisine_list, cuisine);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        // we want multiple clicks
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listview.setOnClickListener();

        findViewById(R.id.save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                registerUser();
        }
    }

    public void registerUser(){
        SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
        int size = checkedItems.size();
        if(size < 1){
            Toast.makeText(this, "Select atleast one cuisine of interest!", Toast.LENGTH_LONG).show();
        }
        else {
            ArrayList<String> interests = new ArrayList<>();
            String item;
            int count = listview.getCount();
            EditText editText = (EditText) findViewById(R.id.phoneNoVal);
            String phoneNo = editText.getText().toString();

            for (int i = 0; i < count; i++) {
                if (checkedItems.get(i)) {
                    item = listview.getItemAtPosition(i).toString();
                    interests.add(item);
                }
            }
            final UserApi userApi = (UserApi) RestService.getService(UserApi.class);
            User user = new User();
            user.setName(loggedInUser.getName());
            user.setEmail(loggedInUser.getEmail());
            user.setImgUrl(loggedInUser.getPicUrl());
            user.setPhoneNo(phoneNo);
            user.setInterest(interests.toArray(new String[0]));
            Call<ResponseBody> response = userApi.createUser(user);
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e(TAG, response.body().string());
                            Intent mainActivityIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
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
    }
    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }
    // [END signOut]

    private static final String TAG = "Register User: ";
}
