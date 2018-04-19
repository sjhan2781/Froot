package com.example.hansangjin.froot.ParcelableData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hansangjin.froot.Data.Restaurant;

/**
 * Created by sjhan on 2018-02-28.
 */

public class ParcelableRestaurant extends Restaurant implements Parcelable {

    public ParcelableRestaurant(int ID, String name, int category) {
        super(ID, name, category);
    }

    public ParcelableRestaurant(Restaurant restaurant) {
        super.setID(restaurant.getID());
        super.setAddress(restaurant.getAddress());
        super.setCategory(restaurant.getCategory());
        super.setDistance(restaurant.getDistance());
        super.setFoods(restaurant.getFoods());
        super.setMapx(restaurant.getMapx());
        super.setMapy(restaurant.getMapy());
        super.setName(restaurant.getName());
        super.setTelephone(restaurant.getTelephone());
        super.setHalal(restaurant.getHalal());
        super.setImage_base64(restaurant.getImage_base64());
    }

    protected ParcelableRestaurant(Parcel in) {
        super.setID(in.readInt());
        super.setAddress(in.readString());
        super.setCategory(in.readInt());
        super.setDistance(in.readDouble());
//        super.setFoods(in.readArrayList().getFoods());
        super.setMapx(in.readDouble());
        super.setMapy(in.readDouble());
        super.setName(in.readString());
        super.setTelephone(in.readString());
        super.setHalal(in.readInt());
        super.setImage_base64(in.readString());
    }

    public static final Creator<ParcelableRestaurant> CREATOR = new Creator<ParcelableRestaurant>() {
        @Override
        public ParcelableRestaurant createFromParcel(Parcel in) {
            return new ParcelableRestaurant(in);
        }

        @Override
        public ParcelableRestaurant[] newArray(int size) {
            return new ParcelableRestaurant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(super.getID());
        dest.writeString(super.getName());
        dest.writeInt(super.getCategory());
        dest.writeString(super.getTelephone());
        dest.writeString(super.getAddress());
        dest.writeDouble(super.getMapx());
        dest.writeDouble(super.getMapy());
        dest.writeInt(super.getHalal());
        dest.writeString(super.getImage_base64());
    }
}
