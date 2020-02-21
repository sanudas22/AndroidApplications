package com.example.intentpractise;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcellableUser implements Parcelable {
    String color;
    double code;

    public ParcellableUser(String color, double code) {
        this.color = color;
        this.code = code;
    }

    @Override
    public String toString() {
        return "ParcellableUser{" +
                "color='" + color + '\'' +
                ", code=" + code +
                '}';
    }

    protected ParcellableUser(Parcel in) {
        this.color = in.readString();
        this.code = in.readDouble();
    }

    public static final Creator<ParcellableUser> CREATOR = new Creator<ParcellableUser>() {
        @Override
        public ParcellableUser createFromParcel(Parcel in) {
            return new ParcellableUser(in);
        }

        @Override
        public ParcellableUser[] newArray(int size) {
            return new ParcellableUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.color);
        dest.writeDouble(this.code);
    }
}
