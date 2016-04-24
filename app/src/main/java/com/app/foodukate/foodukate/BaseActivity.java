package com.app.foodukate.foodukate;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.app.foodukate.common.Environment;
import com.app.foodukate.menu_scanner.OCRApi;
import com.app.foodukate.menu_scanner.OCRService;
import com.app.foodukate.menu_scanner.ScannedListActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    private static final int SIGN_IN_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_camera: {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
                break;
            }
            default:
                break;
        }
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.sign_out:
                signOut();
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
                        Intent login = new Intent(BaseActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }
    // [END signOut]

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.image = (Bitmap) data.getExtras().get("data");
        File file = imageToFile(this.image);

        try {
            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            RequestBody apikey = RequestBody.create(
                    MediaType.parse("multipart/form-data"), Environment.APIKEY);
            RequestBody language = RequestBody.create(
                    MediaType.parse("multipart/form-data"), Environment.LANGUAGE);

            OCRApi ocrApi = (OCRApi) OCRService.getService(OCRApi.class);
            Call<ResponseBody> response = ocrApi.getScannedResults(apikey, language, ISOVERLAYREQUIRED, body);

            handleResponse(response);
        } catch(Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());
        }

    }



    public Bitmap getImage() {
        return this.image;
    }

    private void handleResponse(Call<ResponseBody> response) {
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray parsedResults = jsonObject.getJSONArray("ParsedResults");
                    JSONObject zeroParsedResult = parsedResults.getJSONObject(0);
                    String results = zeroParsedResult.getString("ParsedText");

                    Intent scannedListIntent = new Intent(BaseActivity.this, ScannedListActivity.class);
                    scannedListIntent.putExtra("scannedResults", results);
                    BaseActivity.this.startActivity(scannedListIntent);
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

    private File imageToFile(Bitmap image) {
        File imageFile = new File(this.getFilesDir().getPath(), "image.jpg");

        try {
            //String path = this.getFilesDir().getPath() + "/" + imageFile.getName();
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile.getPath());
            image.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return imageFile;
        } catch (Exception e) {
            System.out.println("Unable to write image file");
            return null;
        }
    }

    private Bitmap image = null;
    private static final boolean ISOVERLAYREQUIRED = false;
    private static final String TAG = "BaseActivity: ";
}
