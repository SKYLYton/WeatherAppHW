package com.weather.model.city;

import com.weather.model.weather.Coord;

public class CityModel {
    private int id;
    private String name;
    private String state;
    private String country;
    private Coord coord;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}
