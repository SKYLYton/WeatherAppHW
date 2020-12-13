package com.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class MainParcel implements Parcelable {

    private boolean isDark;
    private String currentTab;
    private String currentCountry;


    protected MainParcel(Parcel in) {
        isDark = in.readByte() != 0;
        currentTab = in.readString();
        currentCountry = in.readString();
    }

    public MainParcel(boolean isDark, String currentTab, String currentCountry) {
        this.isDark = isDark;
        this.currentTab = currentTab;
        this.currentCountry = currentCountry;
    }

    public static final Creator<MainParcel> CREATOR = new Creator<MainParcel>() {
        @Override
        public MainParcel createFromParcel(Parcel in) {
            return new MainParcel(in);
        }

        @Override
        public MainParcel[] newArray(int size) {
            return new MainParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isDark ? 1 : 0));
        dest.writeString(currentTab);
        dest.writeString(currentCountry);
    }

    public boolean isDark() {
        return isDark;
    }

    public void setDark(boolean dark) {
        isDark = dark;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }
}
