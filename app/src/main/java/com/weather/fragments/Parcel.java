package com.weather.fragments;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private String cityName;
    private int cityId;
    private double lat;
    private double lng;
    private boolean isCord;

    public Parcel(String cityName, int cityId, double lat, double lng, boolean isCord) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.lat = lat;
        this.lng = lng;
        this.isCord = isCord;
    }

    public Parcel(String cityName, int cityId, double lat, double lng) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.lat = lat;
        this.lng = lng;
    }

    public Parcel(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public Parcel(String cityName, int cityId, boolean isCord) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.isCord = isCord;
    }

    public Parcel() {
    }

    protected Parcel(android.os.Parcel in) {
        cityName = in.readString();
        cityId = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
        isCord = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeInt(cityId);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeByte((byte) (isCord ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getCityId() {
        return cityId;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCord(boolean cord) {
        isCord = cord;
    }

    public boolean isCord() {
        return isCord;
    }
}
