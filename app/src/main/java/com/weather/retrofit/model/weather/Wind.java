package com.weather.retrofit.model.weather;

public class Wind {
    private float speed;
    private float deg;

    public float getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
