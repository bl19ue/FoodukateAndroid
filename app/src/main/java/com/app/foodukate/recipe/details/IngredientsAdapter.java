package com.app.foodukate.recipe.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

import java.util.List;

/**
 * Created by pbansal on 4/17/16.
 */
public class IngredientsAdapter extends BaseAdapter{

    public IngredientsAdapter(Context context, List<String> ingredientsList){
        this.context = context;
        this.ingredientsList = ingredientsList;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.ingredient_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String ingredient = ingredientsList.get(position);
        myViewHolder.ingredientName.setText(ingredient);
        return convertView;
    }

    private Context context;
    private List<String> ingredientsList;
    private LayoutInflater layoutInflater;

    private class MyViewHolder{
        TextView ingredientName;
        public MyViewHolder(View item){
            ingredientName = (TextView) item.findViewById(R.id.ingredient_name);
        }
    }
}
