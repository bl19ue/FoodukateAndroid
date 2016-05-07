package com.app.foodukate.recipe.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by pbansal on 4/18/16.
 */
public class NutritionAdapter extends BaseAdapter{

    public NutritionAdapter(Context context, Map<String, String> nutritionMap){
        this.context = context;
        this.nutritionMap = nutritionMap;
        layoutInflater = LayoutInflater.from(this.context);
        this.mapKeys = nutritionMap.keySet().toArray(new String[nutritionMap.size()]);
    }

    @Override
    public int getCount() {
        return nutritionMap.size();
    }

    @Override
    public Object getItem(int position) {
        return nutritionMap.get(mapKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.nutrition_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String nutritionLabel = mapKeys[position];
        String nutritionValue = nutritionMap.get(nutritionLabel).toString();
        myViewHolder.nutritionLabel.setText(nutritionLabel.toUpperCase());
        switch (nutritionLabel){
            case "energy":
                nutritionValue += " kcal";
                break;
            case "iron":
                nutritionValue += " mg";
                break;
            case "calcium":
                nutritionValue += " mg";
                break;
            default:
                nutritionValue += " g";
                break;
        }

        myViewHolder.nutritionValue.setText(nutritionValue);
        return convertView;
    }

    private Context context;
    private Map<String, String> nutritionMap;
    private String[] mapKeys;
    private LayoutInflater layoutInflater;

    private class MyViewHolder{
        TextView nutritionLabel;
        TextView nutritionValue;
        public MyViewHolder(View item){
            nutritionLabel = (TextView) item.findViewById(R.id.nutrition_label);
            nutritionValue = (TextView) item.findViewById(R.id.nutrition_value);
        }
    }
}
