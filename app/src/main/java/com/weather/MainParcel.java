package com.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class MainParcel implements Parcelable {

    private boolean isDark;
    private String currentTab;

    protected MainParcel(Parcel in) {
        isDark = in.readByte() != 0;
        currentTab = in.readString();
    }

    public MainParcel(boolean isDark, String currentTab) {
        this.isDark = isDark;
        this.currentTab = currentTab;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isDark ? 1 : 0));
        parcel.writeString(currentTab);
    }

    public boolean isDark() {
        return isDark;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }
}
