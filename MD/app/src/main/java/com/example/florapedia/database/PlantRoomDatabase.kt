package com.example.florapedia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritePlant::class], version = 1)
abstract class PlantRoomDatabase : RoomDatabase() {
    abstract fun favoritePlantDao(): FavoritePlantDao
    companion object {
        @Volatile
        private var INSTANCE: PlantRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): PlantRoomDatabase {
            if (INSTANCE == null) {
                synchronized(PlantRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PlantRoomDatabase::class.java, "plants_database")
                        .build()
                }
            }
            return INSTANCE as PlantRoomDatabase
        }
    }
}
