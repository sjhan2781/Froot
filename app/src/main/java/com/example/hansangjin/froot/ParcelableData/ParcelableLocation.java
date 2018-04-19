package com.example.hansangjin.froot.ParcelableData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hansangjin.froot.Data.Location;

/**
 * Created by sjhan on 2018-04-19.
 */

public class ParcelableLocation extends Location implements Parcelable {

    public ParcelableLocation(Location location){
        super.setId(location.getId());
        super.setLocation(location.getLocation());
    }

    protected ParcelableLocation(Parcel in) {
        super.setId(in.readInt());
        super.setLocation(in.readString());
    }

    public static final Creator<ParcelableLocation> CREATOR = new Creator<ParcelableLocation>() {
        @Override
        public ParcelableLocation createFromParcel(Parcel in) {
            return new ParcelableLocation(in);
        }

        @Override
        public ParcelableLocation[] newArray(int size) {
            return new ParcelableLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(super.getId());
        dest.writeString(super.getLocation());
    }
}
