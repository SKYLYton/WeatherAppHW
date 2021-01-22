package com.weather;

import android.app.Application;

import androidx.room.Room;

import com.weather.room.model.CitiesDao;
import com.weather.room.model.CitiesDatabase;

public class App extends Application {
    private static App instance;

    // База данных
    private CitiesDatabase db;

    // Получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем объект приложения (для Singleton’а)
        instance = this;

        // Строим базу
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(
                        getApplicationContext(),
                        CitiesDatabase.class,
                        "education_database")
                        .build();
            }
        }).start();
    }

    // Получаем EducationDao для составления запросов
    public CitiesDao getCitiesDao() {
        return db.getEducationDao();
    }

}
