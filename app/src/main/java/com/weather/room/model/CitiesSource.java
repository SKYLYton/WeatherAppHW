package com.weather.room.model;

import android.app.Activity;

import com.weather.App;

import java.util.List;

public class CitiesSource {
    private static CitiesSource instance;
    private OnDataLoadedListener onDataLoadedListener;

    public interface OnDataLoadedListener {
        void onCountCities(long count);
    }

    public void setOnDataLoadedListener(OnDataLoadedListener onDataLoadedListener) {
        this.onDataLoadedListener = onDataLoadedListener;
    }

    public static CitiesSource getInstance() {
        return instance;
    }

    static {
        instance = new CitiesSource();
    }

    private CitiesDao citiesDao;
    private Thread thread;

    private List<City> cities;

    private CitiesSource() {

        citiesDao = App.getInstance().getCitiesDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadCities();
            }
        }).start();
    }

    public List<City> getCities() {
        return cities;
    }

    public void loadCities() {
        cities = citiesDao.getAllCities();
    }

    public void getCountCities(Activity activity) {
        if (onDataLoadedListener == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                long count = getCountCitiesFromDB();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataLoadedListener.onCountCities(count);
                    }
                });
            }
        }).start();
    }

    public long getCountCitiesFromDB() {
        return citiesDao.getCountCities();
    }

    public void replaceCity(City city) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                citiesDao.deleteCityByName(city.getName());

                citiesDao.insertCity(city);

                if (getCountCitiesFromDB() > 3) {
                    citiesDao.deleteFirstRow();
                }

                loadCities();
            }
        }).start();
    }

    public void addCities(City city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                citiesDao.insertCity(city);
                loadCities();
            }
        }).start();
    }

    public void updateCities(City city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                citiesDao.updateCity(city);
                loadCities();
            }
        }).start();
    }

    public void removeCities(long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                citiesDao.deleteCityById(id);
                loadCities();
            }
        }).start();
    }

}
