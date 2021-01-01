package com.weather;

public class SelectedLocation {
    private String cityName;
    private int cityId;
    private double lat;
    private double lng;
    private boolean isCord;

    public SelectedLocation() {
    }

    public SelectedLocation(double lat, double lng, boolean isCord, String cityName) {
        this.lat = lat;
        this.lng = lng;
        this.isCord = isCord;
        this.cityName = cityName;
    }

    public SelectedLocation(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isCord() {
        return isCord;
    }

    public void setCord(boolean cord) {
        isCord = cord;
    }
}
