package com.app.foodukate.recipe.ingredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.ingredient_add_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Ingredient ingredient = ingredientList.get(position);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.context,
                android.R.layout.simple_spinner_item, ingredient.getLabels());

        myViewHolder.ingredientQuantity.setText(ingredient.getQuantity());
        myViewHolder.ingredientName.setText(ingredient.getName());
        myViewHolder.ingredientLabel.setAdapter(spinnerAdapter);
        myViewHolder.removeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO remove item and update list
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        EditText ingredientQuantity;
        Spinner ingredientLabel;
        TextView ingredientName;
        Button removeIngredient;

        public MyViewHolder(View item) {
            ingredientQuantity = (EditText) item.findViewById(R.id.ingredient_row_quantity);
            ingredientLabel = (Spinner) item.findViewById(R.id.ingredient_row_label);
            ingredientName = (TextView) item.findViewById(R.id.ingredient_row_name);
            removeIngredient = (Button) item.findViewById(R.id.ingredient_row_remove_button);
        }
    }

    private Context context;
    private List<Ingredient> ingredientList;
    private LayoutInflater layoutInflater;
}
