package com.app.foodukate.menu_scanner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.foodukate.client.RestService;
import com.app.foodukate.foodukate.BaseActivity;
import com.app.foodukate.foodukate.MainActivity;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.Recipe;
import com.app.foodukate.recipe.RecipeApi;
import com.app.foodukate.recipe.RecipeListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannedListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_list);

        String results = getIntent().getStringExtra("scannedResults");
        final String[] parsedResults = parseResults(results);

        ListView resultsList = (ListView) findViewById(R.id.scanned_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, parsedResults);

        resultsList.setAdapter(adapter);

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String name = parsedResults[position];

                    Intent mainActivityIntent = new Intent(ScannedListActivity.this, MainActivity.class);
                    mainActivityIntent.putExtra("recipe_name", name);
                    ScannedListActivity.this.startActivity(mainActivityIntent);
                } catch(Exception e) {
                    Log.e(TAG, "onItemClickListener: " + e.getMessage());
                }
            }
        });
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

    private String[] parseResults(String results) {
        String[] resultsArray = results.split("\r\n");
        return resultsArray;
    }

    private static final String TAG = "ScannedListActivity";
}
