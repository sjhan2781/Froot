package com.example.hansangjin.froot.ParcelableData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hansangjin.froot.Data.RestaurantType;

/**
 * Created by sjhan on 2018-04-19.
 */

public class ParcelableRestaurantType extends RestaurantType implements Parcelable {

    public ParcelableRestaurantType(RestaurantType restaurantType){
        super.setId(restaurantType.getId());
        super.setType(restaurantType.getType());
    }

    protected ParcelableRestaurantType(Parcel in) {
        super.setId(in.readInt());
        super.setType(in.readString());
    }

    public static final Creator<ParcelableRestaurantType> CREATOR = new Creator<ParcelableRestaurantType>() {
        @Override
        public ParcelableRestaurantType createFromParcel(Parcel in) {
            return new ParcelableRestaurantType(in);
        }

        @Override
        public ParcelableRestaurantType[] newArray(int size) {
            return new ParcelableRestaurantType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(super.getId());
        dest.writeString(super.getType());
    }
}
