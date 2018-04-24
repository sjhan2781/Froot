package com.example.hansangjin.froot.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 2. 5..
 */

public class RestaurantMapRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantMapRecyclerViewAdapter.MyViewHolder>
        implements View.OnClickListener{
    private static int selectedPos = 0;
    private ArrayList<ParcelableRestaurant> restaurantList = new ArrayList<>();
    private RestaurantMapActivity activity;

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;


    public interface ItemClick {
        public void onClick(View view,int position);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public RestaurantMapRecyclerViewAdapter(ArrayList<ParcelableRestaurant> restaurantList, RestaurantMapActivity activity) {
        this.restaurantList = restaurantList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_resaurant_map, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final int curPos = position;
        int image_width = holder.imageView_restaurant.getLayoutParams().width;
        int image_height = holder.imageView_restaurant.getLayoutParams().height;


        holder.textView_restaurant_name.setText(restaurantList.get(position).getName());
        holder.textView_restaurant_category.setText(ApplicationController.getRestaurantTypes().get(restaurantList.get(position).getCategory()).getType());
        holder.textView_restaurant_phonenumber.setText(Html.fromHtml("<u>" + restaurantList.get(position).getTelephone() + "</u>"));
        holder.textView_restaurant_distance.setText(String.valueOf(restaurantList.get(position).getConversionDistance()));
        holder.textView_restaurant_address.setText(restaurantList.get(position).getAddress());


        if(!restaurantList.get(position).getImage_base64().isEmpty()) {
            holder.imageView_restaurant.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(restaurantList.get(position).getImage_base64()), image_width, image_height, true));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, curPos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(RestaurantMapActivity.this, "LongClickListener", Toast.LENGTH_LONG).show();

                return true;
            }
        });


    }

    public ArrayList<ParcelableRestaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<ParcelableRestaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int position) {
        this.notifyItemChanged(selectedPos);
        this.selectedPos = position;
        this.notifyItemChanged(selectedPos);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView textView_restaurant_name, textView_restaurant_category,
                textView_restaurant_phonenumber, textView_restaurant_distance, textView_restaurant_address;
        private ImageView imageView_restaurant;


        public MyViewHolder(View parent) {
            super(parent);

            parent.setClipToOutline(false);

            this.imageView_restaurant = parent.findViewById(R.id.imageView_restaurant);
            this.textView_restaurant_name = parent.findViewById(R.id.textView_restaurant_name);
            this.textView_restaurant_category = parent.findViewById(R.id.textView_restaurant_category);
            this.textView_restaurant_phonenumber = parent.findViewById(R.id.textView_restaurant_phonenumber);
            this.textView_restaurant_distance = parent.findViewById(R.id.textView_distance);
            this.textView_restaurant_address = parent.findViewById(R.id.textView_review_address);
        }
    }

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
