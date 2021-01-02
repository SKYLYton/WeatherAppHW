package com.weather.retrofit.model;

import com.weather.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static final RetrofitRequest INSTANCE;
    private final OpenWeather OPEN_WEATHER;

    static {
        INSTANCE = new RetrofitRequest();
    }

    private RetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_CONNECTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OPEN_WEATHER = retrofit.create(OpenWeather.class);
    }

    public static RetrofitRequest getInstance() {
        return INSTANCE;
    }

    public OpenWeather getOpenWeather(){
        return OPEN_WEATHER;
    }
}
