package com.example.earthquakes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.earthquakes.Earthquake;

import java.util.List;

@Dao
public interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Earthquake> eartquakeList);

    @Query("SELECT * FROM earthquakes")
    LiveData<List<Earthquake>> getEarthquakes();
}
