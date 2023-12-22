package com.example.florapedia.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoritePlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoritePlant: FavoritePlant)

    @Delete
    fun delete(favoritePlant: FavoritePlant)

    @Query("SELECT * from favoritePlant")
    fun getAllPlants(): LiveData<List<FavoritePlant>>
}