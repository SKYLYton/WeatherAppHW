package com.weather;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private String cityName;
    private boolean allInf;

    protected Parcel(android.os.Parcel in) {
        cityName = in.readString();
        allInf = in.readByte() != 0;
    }

    public Parcel(String cityName, boolean allInf) {
        this.cityName = cityName;
        this.allInf = allInf;
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
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeByte((byte) (allInf ? 1 : 0));
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isAllInf() {
        return allInf;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setAllInf(boolean allInf) {
        this.allInf = allInf;
    }
}
