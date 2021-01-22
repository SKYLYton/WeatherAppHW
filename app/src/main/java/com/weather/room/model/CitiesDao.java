package com.weather.room.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CitiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(City city);

    // Метод для замены данных студента
    @Update
    void updateCity(City city);

    // Удаляем данные студента
    @Delete
    void deleteCity(City city);

    // Удаляем данные студента, зная ключ
    @Query("DELETE FROM city WHERE id = :id")
    void deleteCityById(long id);

    @Query("DELETE FROM city WHERE id = (SELECT id FROM city LIMIT 1)")
    void deleteFirstRow();

    // Забираем данные по всем студентам
    @Query("SELECT * FROM city")
    List<City> getAllCities();

    // Получаем данные одного студента по id
    @Query("SELECT * FROM city WHERE id = :id")
    City getCityById(long id);

/*    @Query("SELECT id FROM city WHERE name = :name")
    long getIdByName(String name);*/

    @Query("DELETE FROM city WHERE id = (SELECT id FROM city WHERE name = :name)")
    void deleteCityByName(String name);

    //Получаем количество записей в таблице
    @Query("SELECT COUNT() FROM city")
    long getCountCities();

}
