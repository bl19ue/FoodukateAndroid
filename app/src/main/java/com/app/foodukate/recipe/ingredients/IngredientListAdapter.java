package com.app.foodukate.recipe.ingredients;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;
import com.app.foodukate.recipe.AddRecipeActivity;

import java.util.List;

/**
 * Created by sumitvalecha on 4/22/16.
 */
public class IngredientListAdapter extends BaseAdapter {

    public IngredientListAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.ingredient_add_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Ingredient ingredient = AddRecipeActivity.savedIngredientList.get(position);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.context,
                android.R.layout.simple_spinner_item, ingredient.getLabels());

        myViewHolder.reference = position;
        myViewHolder.ingredientQuantity.setText(ingredient.getQuantity());
        myViewHolder.ingredientName.setText(ingredient.getName());
        myViewHolder.ingredientLabel.setAdapter(spinnerAdapter);
        myViewHolder.removeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipeActivity.savedIngredientList.remove(ingredient);
                AddRecipeActivity.ingredientListAdapter.notifyDataSetChanged();
            }
        });

        myViewHolder.ingredientQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Ingredient thisIngredient = AddRecipeActivity.savedIngredientList.get(myViewHolder.reference);
                if (thisIngredient != null) {
                    thisIngredient.setQuantity(myViewHolder.ingredientQuantity.getText().toString());

                    AddRecipeActivity.savedIngredientList.set(myViewHolder.reference, thisIngredient);
                    AddRecipeActivity.ingredientListAdapter.notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        EditText ingredientQuantity;
        Spinner ingredientLabel;
        TextView ingredientName;
        ImageButton removeIngredient;
        int reference;

        public MyViewHolder(View item) {
            ingredientQuantity = (EditText) item.findViewById(R.id.ingredient_row_quantity);
            ingredientLabel = (Spinner) item.findViewById(R.id.ingredient_row_label);
            ingredientName = (TextView) item.findViewById(R.id.ingredient_row_name);
            removeIngredient = (ImageButton) item.findViewById(R.id.ingredient_row_remove_button);
        }
    }

    private Context context;
    private List<Ingredient> ingredientList;
    private LayoutInflater layoutInflater;
}
