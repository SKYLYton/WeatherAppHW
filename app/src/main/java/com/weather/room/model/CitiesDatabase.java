package com.weather.room.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class CitiesDatabase  extends RoomDatabase {
    public abstract CitiesDao getEducationDao();
}
