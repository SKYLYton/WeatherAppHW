package com.weather.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name"})})
public class City {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "temperature")
    public float temperature;

    @ColumnInfo(name = "pressure")
    public int pressure;

    @ColumnInfo(name = "wind")
    public float wind;

    public City(String name, float temperature, int pressure, float wind) {
        this.name = name;
        this.temperature = temperature;
        this.pressure = pressure;
        this.wind = wind;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public float getWind() {
        return wind;
    }
}
