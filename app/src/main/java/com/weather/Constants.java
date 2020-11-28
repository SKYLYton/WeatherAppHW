package com.weather;

public class Constants {
    public static final boolean VERBOSE = true;
    public static final boolean DEBUG = VERBOSE && true;

    public static final String API_KEY_NAME = "appid";
    public static final String API_CITY_ID_NAME = "id";

    //!!!!!!
    public static final String API_KEY = "Your api key";
    //!!!!!!

    public static final String API_LANGUAGE_NAME = "lang";
    public static final String API_LANGUAGE_RU = "ru";
    public static final String API_UNITS_NAME = "units";
    public static final String API_UNITS_METRIC = "metric";
    public static final String URL_CONNECTION = "https://api.openweathermap.org/data/2.5/weather";
    public static final int DEFAULT_CITY_ID = 524901;

    public static final String EMPTY_STRING = "";

    public static final String SHARED_THEME_IS_DARK = "SHARED_THEME_IS_DARK";

    public static final String MAIN_SHARED_NAME = "MAIN_SHARED_NAME";
    public static final String SHARED_IS_COUNTRY_EMPTY = "SHARED_IS_COUNTRY_EMPTY";

    public static final String SHARED_COUNTRY_NAME = "COUNTRY_NAME";
    public static final String SHARED_COUNTRY_ID = "COUNTRY_ID";

    public static final String CURRENT_PARCEL = "CURRENT_PARCEL";

    public static final String FILE_CITIES_NAME = "cityList.json";
}
