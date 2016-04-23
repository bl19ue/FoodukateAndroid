package com.app.foodukate.recipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodukate.client.RestService;
import com.app.foodukate.common.MultiSpinner;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.ingredients.Ingredient;
import com.app.foodukate.recipe.ingredients.IngredientListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeName = (EditText) findViewById(R.id.new_recipe_name);
//        recipeCuisine = (EditText) findViewById(R.id.new_recipe_cuisine);
//        recipeCourse = (EditText) findViewById(R.id.new_recipe_course);
        recipeSteps = (EditText) findViewById(R.id.new_recipe_steps);

        recipeCuisineMulti = (MultiSpinner) findViewById(R.id.new_recipe_cuisine_multi);
        recipeCuisineMulti.setItems(cuisines);

        uploadRecipeButton = (Button) findViewById(R.id.new_recipe_upload_image_button);
        uploadRecipeButton.setOnClickListener(this);

        newIngredientButton = (Button) findViewById(R.id.new_recipe_ingredients_button);
        newIngredientButton.setOnClickListener(this);

        recipeImage = (ImageView) findViewById(R.id.new_recipe_image);

        ingredientListAdapter = new IngredientListAdapter(this, savedIngredientList);

        ingredientDialog = new Dialog(this);

        ingredientDialog.setContentView(R.layout.dialog_ingredients);
        ingredientDialog.setTitle("Add ingredients");


        ingredientName = (AutoCompleteTextView) ingredientDialog.findViewById(R.id.ingredient_name);

        ingredientsListView = (ListView) ingredientDialog.findViewById(R.id.new_recipe_ingredients_list);
        ingredientsListView.setAdapter(ingredientListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_recipe, menu);
        return true;
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.new_recipe_upload_image_button: {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
            }

            case R.id.new_recipe_ingredients_button: {
                createIngredientsDialog(AddRecipeActivity.this);
                break;
            }
        }
    }


    public void createIngredientsDialog(final Context activity) {
        ImageButton addIngredientButton = (ImageButton) ingredientDialog.findViewById(R.id.add_ingredient_button);

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ingredientNameChanged) {
                    String name = ingredientName.getText().toString();
                    Ingredient ingredient = null;
                    for (int i = 0; i < ingredientArrayList.size(); i++) {
                        if (ingredientArrayList.get(i).getName().equals(name)) {
                            ingredient = ingredientArrayList.get(i);
                            break;
                        }
                    }

                    if (ingredient != null) {
                        savedIngredientList.add(ingredient);
                        ingredientListAdapter.notifyDataSetChanged();
                        ingredientName.clearListSelection();
                        ingredientName.setText("");
                    }

                } else {
                    Toast.makeText(AddRecipeActivity.this, "Select from list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ingredientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredientsNameList = new ArrayList<String>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    ingredientNameChanged = true;
                    String name = ingredientName.getText().toString();
                    Call<ResponseBody> ingredientsResponse = recipeApi.getIngredientsByName(name);
                    handleIngredientsResponse(ingredientsResponse);
                }
            }
        });

        ingredientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingredientNameChanged = false;
                ingredientName.dismissDropDown();
            }
        });

        ingredientDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                recipeImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(TAG, "onActivityResult: IOException: " + e.getMessage());
            }
        }
    }

    private void handleIngredientsResponse(Call<ResponseBody> ingredientsCall) {
        ingredientsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ingredientArrayList = new ArrayList<>();
                    ingredientsNameList = new ArrayList<>();

                    JSONArray ingredientsJSONArray = new JSONArray(response.body().string());
                    for(int i=0;i<ingredientsJSONArray.length();i++) {
                        JSONObject ingredientJSONObject = ingredientsJSONArray.getJSONObject(i);
                        String _id = ingredientJSONObject.getString("_id");
                        String name = ingredientJSONObject.getString("name");

                        JSONArray labels = ingredientJSONObject.getJSONArray("labels");
                        ArrayList<String> labelsArray = new ArrayList<>();
                        for(int j=0;j<labels.length();j++) {
                            labelsArray.add(labels.getString(j));
                        }

                        ingredientArrayList.add(new Ingredient(_id, name, labelsArray));
                        ingredientsNameList.add(name);
                    }

                    ingredientName.setAdapter(new ArrayAdapter<String>(AddRecipeActivity.this,
                            android.R.layout.simple_dropdown_item_1line, ingredientsNameList));
                    ingredientName.showDropDown();

                } catch (JSONException e) {
                    Log.e(TAG, "handleIngredientsResponse: JSONException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "handleIngredientsResponse: IOException: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "handleIngredientsResponse: onFailure: " + t.getMessage());
            }
        });
    }

    private EditText recipeName;
//    private EditText recipeCuisine;
//    private EditText recipeCourse;
    private EditText recipeSteps;

    private MultiSpinner recipeCuisineMulti;

    private Button uploadRecipeButton;
    private Button newIngredientButton;
    private Button addIngredientButton;

    private ImageView recipeImage;

    private ListView ingredientsListView;

    private AutoCompleteTextView ingredientName;

    private Bitmap bitmap = null;

    private Dialog ingredientDialog;

    private String[] cuisines = {"Asian","Barbecue","Greek","Mediterranean","Indian","Kid-Friendly",
            "Thai","Mexican","Spanish","Japanese","American","Chinese","Italian",
            "Southwestern","Moroccan","Cuban","English","French","Hungarian","German",
            "Swedish","Hawaiian","Cajun & Creole","Portuguese","Irish","Southern & Soul Food"};
    private final int PICK_IMAGE_REQUEST = 1;
    private final String TAG = "AddRecipeActivity: ";
    private ArrayList<String> ingredientsNameList;
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();;
    private final RecipeApi recipeApi = (RecipeApi) RestService.getService(RecipeApi.class);
    private boolean ingredientNameChanged = true;

    public static ArrayList<Ingredient> savedIngredientList = new ArrayList<>();
    public static IngredientListAdapter ingredientListAdapter;
}
