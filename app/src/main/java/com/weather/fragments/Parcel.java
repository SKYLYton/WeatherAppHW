package com.weather.fragments;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private String cityName;
    private int cityId;

    public Parcel(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    protected Parcel(android.os.Parcel in) {
        cityName = in.readString();
        cityId = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeInt(cityId);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
