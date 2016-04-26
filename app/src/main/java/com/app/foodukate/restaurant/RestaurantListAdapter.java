package com.app.foodukate.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.foodukate.foodukate.R;

import java.util.List;

/**
 * Created by pbansal on 4/25/16.
 */
public class RestaurantListAdapter extends BaseAdapter{

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantList){
        this.context = context;
        this.restaurantList = restaurantList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.restaurant_row, parent,false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        final Restaurant restaurant = restaurantList.get(position);
        myViewHolder.restaurantName.setText(restaurant.getName());
        myViewHolder.restaurantLocation.setText("Test Location");
        myViewHolder.restaurantAddress.setText("Test Address");
        return convertView;
    }

    private Context context;
    private List<Restaurant> restaurantList;
    private LayoutInflater layoutInflater;

    private class MyViewHolder{
        TextView restaurantName, restaurantLocation, restaurantAddress;

        public MyViewHolder(View item){
            restaurantName = (TextView) item.findViewById(R.id.restaurant_name);
            restaurantAddress = (TextView) item.findViewById(R.id.restaurant_address);
            restaurantLocation = (TextView) item.findViewById(R.id.restaurant_location);
        }
    }
}
