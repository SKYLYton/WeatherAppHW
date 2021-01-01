package com.weather.model.weather;

import com.weather.Constants;
import com.weather.model.OpenWeather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static RetrofitRequest instance;
    private Retrofit retrofit;
    private OpenWeather openWeather;

    static {
        instance = new RetrofitRequest();
    }

    private RetrofitRequest() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_CONNECTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public static RetrofitRequest getInstance() {
        return instance;
    }


    public OpenWeather getOpenWeather(){
        return openWeather;
    }
}
