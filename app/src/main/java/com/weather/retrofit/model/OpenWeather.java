package com.weather.retrofit.model;

import com.weather.retrofit.model.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> getWeather(@Query("units") String units, @Query("lang") String lang,
                                    @Query("id") int cityId, @Query("appid") String apiKey);

    @GET("data/2.5/weather")
    Call<WeatherRequest> getWeather(@Query("units") String units, @Query("lang") String lang,
                                    @Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey);
}
