package com.example.rabinovich.schoolbus.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.rabinovich.schoolbus.Database.Driver;

@Dao
public interface DriverDao {
    @Insert
    void insert(Driver driver);

    @Query("SELECT * FROM driver WHERE driver.user_id = :driver_id")
    Driver getDriverById(int driver_id);
}
