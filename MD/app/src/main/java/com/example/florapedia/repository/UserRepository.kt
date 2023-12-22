package com.example.florapedia.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.florapedia.data.remote.ApiService
import com.example.florapedia.data.response.PlantDetailResponse
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.database.FavoritePlantDao
import com.example.florapedia.database.PlantRoomDatabase
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository private constructor(
    application: Application,
    private val apiService: ApiService,
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var mFavoritePlantDao: FavoritePlantDao? = null
    init {
        val db = PlantRoomDatabase.getDatabase(application)
        mFavoritePlantDao = db.favoritePlantDao()
    }

    suspend fun getPlants(): Response<PlantDetailResponse> {
        return apiService.getPlants()
    }

    fun getFavoritePlants(): LiveData<List<FavoritePlant>>? {
        return mFavoritePlantDao?.getAllPlants()
    }

    suspend fun getDetailPlant(id: String): Response<PlantDetailResponse> {
        return apiService.getDetailPlant(id)
    }

    fun getAllPlants(): LiveData<List<FavoritePlant>>? = mFavoritePlantDao?.getAllPlants()

    fun insert(favoritePlant: FavoritePlant) {
        executorService.execute { mFavoritePlantDao?.insert(favoritePlant) }
    }

    fun delete(favoritePlant: FavoritePlant) {
        executorService.execute { mFavoritePlantDao?.delete(favoritePlant) }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            application: Application,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(application, apiService)
            }.also { instance = it }
    }
}