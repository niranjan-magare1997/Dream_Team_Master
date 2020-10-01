package com.example.dream_team.constants;

import android.os.Parcel;
import android.os.Parcelable;

public class DishConstant implements Parcelable {
    public String name;

    public DishConstant(String name) {
        this.name = name;
    }

    protected DishConstant(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DishConstant> CREATOR = new Creator<DishConstant>() {
        @Override
        public DishConstant createFromParcel(Parcel in) {
            return new DishConstant(in);
        }

        @Override
        public DishConstant[] newArray(int size) {
            return new DishConstant[size];
        }
    };
}
