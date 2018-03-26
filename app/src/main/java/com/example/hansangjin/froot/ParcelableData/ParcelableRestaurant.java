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
        super.setDescription(restaurant.getDescription());
        super.setDistance(restaurant.getDistance());
        super.setFoods(restaurant.getFoods());
        super.setLink(restaurant.getLink());
        super.setMapx(restaurant.getMapx());
        super.setMapy(restaurant.getMapy());
        super.setName(restaurant.getName());
        super.setTelephone(restaurant.getTelephone());
    }

    protected ParcelableRestaurant(Parcel in) {
        super.setName(in.readString());
        super.setAddress(in.readString());
        super.setCategory(in.readInt());
        super.setLink(in.readString());
        super.setTelephone(in.readString());
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
        dest.writeString(super.getName());
        dest.writeString(super.getAddress());
        dest.writeInt(super.getCategory());
        dest.writeString(super.getLink());
        dest.writeString(super.getTelephone());
    }
}
