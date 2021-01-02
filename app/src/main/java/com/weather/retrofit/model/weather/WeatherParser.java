package com.weather.retrofit.model.weather;

import com.google.gson.Gson;

public class WeatherParser {
    private String result;
    private WeatherRequest weatherRequest;

    public WeatherParser(String result) {
        this.result = result;
        Gson gson = new Gson();
        weatherRequest = gson.fromJson(result, WeatherRequest.class);
    }

    public WeatherRequest getWeatherRequest() {
        return weatherRequest;
    }
}
