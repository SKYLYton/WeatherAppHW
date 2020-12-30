package com.weather.model.weather;

import com.weather.Constants;
import com.weather.model.OpenWeather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    public static OpenWeather getOpenWeather(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_CONNECTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeather openWeather = retrofit.create(OpenWeather.class);
        return openWeather;
    }
}
