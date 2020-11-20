package com.weather.fragments;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private String cityName;

    protected Parcel(android.os.Parcel in) {
        cityName = in.readString();
    }

    public Parcel(String cityName) {
        this.cityName = cityName;
    }


    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(cityName);
    }
}
