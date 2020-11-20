package com.weather.adapters;

public class DayItem {
    private String dayName;
    private String date;
    private String temperature;

    public DayItem(String dayName, String date, String temperature) {
        this.dayName = dayName;
        this.date = date;
        this.temperature = temperature;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
