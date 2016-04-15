package com.app.foodukate.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.foodukate.foodukate.R;
import com.app.foodukate.volley.VolleySingleton;

import java.util.List;

/**
 * Created by sumitvalecha on 3/14/16.
 */
public class RecipeListAdapter extends BaseAdapter {

    public RecipeListAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        layoutInflater = LayoutInflater.from(this.context);
        imageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.recipe_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Recipe recipe = recipeList.get(position);
        myViewHolder.recipeName.setText(recipe.getName());
        myViewHolder.recipeImage.setImageUrl(recipe.getImageUrl(), imageLoader);
        myViewHolder.recipeImage.setOnClickListener(new RecipeItemClickListener(recipe.getId()));

        return convertView;
    }


    private Context context;
    private List<Recipe> recipeList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    private class MyViewHolder {
        TextView recipeName;
        NetworkImageView recipeImage;

        public MyViewHolder(View item) {
            recipeName = (TextView) item.findViewById(R.id.recipe_name);
            recipeImage = (NetworkImageView) item.findViewById(R.id.recipe_image);
        }
    }

    class RecipeItemClickListener implements View.OnClickListener {

        public RecipeItemClickListener(String recipeID) {
            this.recipeID = recipeID;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, RecipeDetailActivity.class);
            i.putExtra("recipe_id", this.recipeID);
            context.startActivity(i);
        }

        private String recipeID;
    }
}
