package com.app.foodukate.recipe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.foodukate.client.RestService;
import com.app.foodukate.common.MultiSpinner;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.ingredients.Ingredient;
import com.app.foodukate.recipe.ingredients.IngredientListAdapter;
import com.app.foodukate.secret.AWS;
import com.app.foodukate.user.UserSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Ingredient> savedIngredientList = new ArrayList<>();
    public static IngredientListAdapter ingredientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initializeElements();
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
        switch (v.getId()) {
            case R.id.fab_upload_image_button: {
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
            case R.id.new_recipe_submit_button: {
//                JSONObject recipeObject = createRecipeJSONObject();
                Recipe newRecipe = createRecipeObject();
                if (newRecipe != null) {
                    Call<ResponseBody> addRecipeResponse = recipeApi.addRecipe(newRecipe);
                    handleAddRecipeResponse(addRecipeResponse);
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                recipeImage.getLayoutParams().height = 1400;
                recipeImage.setLayoutParams(recipeImage.getLayoutParams());
                recipeImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(TAG, "onActivityResult: IOException: " + e.getMessage());
            }
        }
    }

    private Recipe createRecipeObject() {
        String name = recipeName.getText().toString();
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> cuisines = new ArrayList<>(recipeCuisineMulti.getSelectedStrings());
        ArrayList<String> categories = new ArrayList<>();

        if (dessertsCheckbox.isChecked()) {
            courses.add(dessertsCheckbox.getText().toString());
        }

        if (soupCheckbox.isChecked()) {
            courses.add(soupCheckbox.getText().toString());
        }

        if (appetizerCheckbox.isChecked()) {
            courses.add(appetizerCheckbox.getText().toString());
        }

        if (beverageCheckbox.isChecked()) {
            courses.add(beverageCheckbox.getText().toString());
        }

        if (entreeCheckbox.isChecked()) {
            courses.add(entreeCheckbox.getText().toString());
        }

        if (sidesCheckbox.isChecked()) {
            courses.add(sidesCheckbox.getText().toString());
        }

        if (breakfastCheckbox.isChecked()) {
            courses.add(breakfastCheckbox.getText().toString());
        }

        if (categoryGlutenFreeCheckbox.isChecked()) {
            categories.add(categoryGlutenFreeCheckbox.getText().toString());
        }

        if (categoryNonVegCheckbox.isChecked()) {
            categories.add(categoryNonVegCheckbox.getText().toString());
        }
        if (categoryVeganCheckbox.isChecked()) {
            categories.add(categoryVeganCheckbox.getText().toString());
        }
        if (categoryVegeterianCheckbox.isChecked()) {
            categories.add(categoryVegeterianCheckbox.getText().toString());
        }

        ArrayList<String> stepsArray = new ArrayList<>();
        String[] steps = recipeSteps.getText().toString().split("\n");
        for (String step : steps) {
            stepsArray.add(step);
        }

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < savedIngredientList.size(); i++) {
            ingredients.add(savedIngredientList.get(i));
        }

        RecipeBuilder recipeBuilder = new RecipeBuilder()
                .withName(name)
                .withCourses(courses)
                .withCategories(categories)
                .withServings(servings.getText().toString())
                .withCuisines(cuisines)
                .withSource(UserSingleton.getInstance().getEmail())
                .withCookingTime(cookingTime.getText().toString())
                .withSteps(stepsArray)
                .withIngredients(ingredients);

        try {
            if (recipeImage != null && recipeImage.getDrawable() != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) recipeImage.getDrawable();
                Bitmap image = bitmapDrawable.getBitmap();

                String imageName = UUID.randomUUID().toString() + ".jpg";
                File file = imageToFile(image, imageName);

                new S3Task().execute(file);

                recipeBuilder.withImageUrl("http://foodukate.s3-us-west-1.amazonaws.com/" + imageName);
            } else {
                recipeBuilder.withImageUrl("");
            }
        } catch (Exception e) {
            Log.e(TAG, "createRecipeJSONObject: Image:" + e.getMessage());
        }

        Recipe newRecipe = recipeBuilder.build();
        return newRecipe;

    }

    private class S3Task extends AsyncTask<File, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(File... params) {
            File image = params[0];

            AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(
                    AWS.accessKeyId,
                    AWS.secretAccessKey));

            try {
                System.out.println("Uploading a new object to S3 from a file\n");
                s3client.putObject(new PutObjectRequest("foodukate", image.getName(), image));

            } catch (AmazonServiceException ase) {
                System.out
                        .println("Caught an AmazonServiceException, which "
                                + "means your request made it "
                                + "to Amazon S3, but was rejected with an error response"
                                + " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: "
                        + ase.getStatusCode());
                System.out.println("AWS Error Code:   "
                        + ase.getErrorCode());
                System.out.println("Error Type:       "
                        + ase.getErrorType());
                System.out.println("Request ID:       "
                        + ase.getRequestId());
                Toast.makeText(AddRecipeActivity.this, "S3 did not work", Toast.LENGTH_SHORT).show();
            } catch (AmazonClientException ace) {
                System.out
                        .println("Caught an AmazonClientException, which "
                                + "means the client encountered "
                                + "an internal error while trying to "
                                + "communicate with S3, "
                                + "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
                Toast.makeText(AddRecipeActivity.this, "S3 did not work", Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }

    private File imageToFile(Bitmap image, String name) {
        File imageFile = new File(this.getFilesDir().getPath(), name);

        try {
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

    private void handleAddRecipeResponse(final Call<ResponseBody> recipeCall) {
        recipeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject recipeObject = new JSONObject(response.body().string());
                    String id = recipeObject.getJSONObject("data").getString("id");

                    Intent recipeDetailIntent = new Intent(AddRecipeActivity.this, RecipeDetailActivity.class);
                    recipeDetailIntent.putExtra("recipe_id", id);
                    startActivity(recipeDetailIntent);
                } catch (JSONException e) {
                    Log.e(TAG, "handleAddRecipeResponse: JSONException:" + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "handleAddRecipeResponse: IOException:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "handleAddRecipeResponse: onFailure:" + t.getMessage());
            }
        });
    }

    private void handleIngredientsResponse(Call<ResponseBody> ingredientsCall) {
        ingredientsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ingredientArrayList = new ArrayList<>();
                    ingredientsNameList = new ArrayList<>();

                    JSONArray ingredientsJSONArray = new JSONArray(response.body().string());
                    for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                        JSONObject ingredientJSONObject = ingredientsJSONArray.getJSONObject(i);
                        String _id = ingredientJSONObject.getString("_id");
                        String name = ingredientJSONObject.getString("name");

                        JSONArray labels = ingredientJSONObject.getJSONArray("labels");
                        ArrayList<String> labelsArray = new ArrayList<>();
                        for (int j = 0; j < labels.length(); j++) {
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
                } catch (Exception e) {
                    Log.e(TAG, "handleIngredientsResponse: Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "handleIngredientsResponse: onFailure: " + t.getMessage());
            }
        });
    }

    public void createIngredientsDialog(final Context activity) {
        ImageButton addIngredientButton = (ImageButton) ingredientDialog.findViewById(R.id.add_ingredient_button);
        ImageButton cancelDialogButton = (ImageButton) ingredientDialog.findViewById(R.id.cancel_button);
        ImageButton confirmDialogButton = (ImageButton) ingredientDialog.findViewById(R.id.confirm_button);

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

        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientDialog.hide();
            }
        });

        confirmDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientDialog.hide();
            }
        });

        ingredientDialog.show();
    }

    private void initializeElements() {
        recipeName = (EditText) findViewById(R.id.new_recipe_name);
//        recipeCuisine = (EditText) findViewById(R.id.new_recipe_cuisine);
//        recipeCourse = (EditText) findViewById(R.id.new_recipe_course);
        recipeSteps = (EditText) findViewById(R.id.new_recipe_steps);
        servings = (EditText) findViewById(R.id.new_recipe_serving);
        cookingTime = (EditText) findViewById(R.id.new_recipe_cooking_time);

        recipeCuisineMulti = (MultiSpinner) findViewById(R.id.new_recipe_cuisine_multi);
        if (recipeCuisineMulti != null) {
            recipeCuisineMulti.setItems(cuisines);
        }

        newIngredientButton = (Button) findViewById(R.id.new_recipe_ingredients_button);
        if (newIngredientButton != null) {
            newIngredientButton.setOnClickListener(this);
        }

        addRecipeButton = (ImageButton) findViewById(R.id.new_recipe_submit_button);
        if (addRecipeButton != null) {
            addRecipeButton.setOnClickListener(this);
        }

        fabUploadImageButton = (FloatingActionButton) findViewById(R.id.fab_upload_image_button);
        if (fabUploadImageButton != null) {
            fabUploadImageButton.setOnClickListener(this);
        }

        dessertsCheckbox = (CheckBox) findViewById(R.id.course_dessert);
        soupCheckbox = (CheckBox) findViewById(R.id.course_soup);
        entreeCheckbox = (CheckBox) findViewById(R.id.course_entree);
        beverageCheckbox = (CheckBox) findViewById(R.id.course_beverage);
        breakfastCheckbox = (CheckBox) findViewById(R.id.course_breakfast);
        sidesCheckbox = (CheckBox) findViewById(R.id.course_sides);
        appetizerCheckbox = (CheckBox) findViewById(R.id.course_appetizer);

        categoryGlutenFreeCheckbox = (CheckBox) findViewById(R.id.category_gluten_free);
        categoryNonVegCheckbox = (CheckBox) findViewById(R.id.category_non_veg);
        categoryVegeterianCheckbox = (CheckBox) findViewById(R.id.category_vegeterian);
        categoryVeganCheckbox = (CheckBox) findViewById(R.id.category_vegan);

        recipeImage = (ImageView) findViewById(R.id.new_recipe_image);

        ingredientListAdapter = new IngredientListAdapter(this, savedIngredientList);

        ingredientDialog = new Dialog(this);

        ingredientDialog.setContentView(R.layout.dialog_ingredients);
        ingredientDialog.setTitle("Add ingredients");


        ingredientName = (AutoCompleteTextView) ingredientDialog.findViewById(R.id.ingredient_name);

        ingredientsListView = (ListView) ingredientDialog.findViewById(R.id.new_recipe_ingredients_list);
        ingredientsListView.setAdapter(ingredientListAdapter);
    }

    private EditText recipeName;
    //    private EditText recipeCuisine;
//    private EditText recipeCourse;
    private EditText recipeSteps;
    private EditText servings;
    private EditText cookingTime;

    private MultiSpinner recipeCuisineMulti;

    private Button newIngredientButton;
    private Button addIngredientButton;
    private ImageButton addRecipeButton;

    private FloatingActionButton fabUploadImageButton;

    private CheckBox dessertsCheckbox;
    private CheckBox soupCheckbox;
    private CheckBox entreeCheckbox;
    private CheckBox beverageCheckbox;
    private CheckBox breakfastCheckbox;
    private CheckBox sidesCheckbox;
    private CheckBox appetizerCheckbox;

    private CheckBox categoryVeganCheckbox;
    private CheckBox categoryVegeterianCheckbox;
    private CheckBox categoryGlutenFreeCheckbox;
    private CheckBox categoryNonVegCheckbox;

    private ImageView recipeImage;

    private ListView ingredientsListView;

    private AutoCompleteTextView ingredientName;

    private Bitmap bitmap = null;

    private Dialog ingredientDialog;

    private ArrayList<String> courses = new ArrayList<>();

    private String[] cuisines = {"Asian", "Barbecue", "Greek", "Mediterranean", "Indian", "Kid-Friendly",
            "Thai", "Mexican", "Spanish", "Japanese", "American", "Chinese", "Italian",
            "Southwestern", "Moroccan", "Cuban", "English", "French", "Hungarian", "German",
            "Swedish", "Hawaiian", "Cajun & Creole", "Portuguese", "Irish", "Southern & Soul Food"};
    private final int PICK_IMAGE_REQUEST = 1;
    private final String TAG = "AddRecipeActivity: ";
    private ArrayList<String> ingredientsNameList;
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ;
    private final RecipeApi recipeApi = (RecipeApi) RestService.getService(RecipeApi.class);
    private boolean ingredientNameChanged = true;

}
