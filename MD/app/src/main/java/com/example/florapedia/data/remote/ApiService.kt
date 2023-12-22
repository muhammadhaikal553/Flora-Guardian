package com.example.florapedia.data.remote

import com.example.florapedia.data.response.PlantDetailResponse
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Path

interface ApiService {
    @GET("allPlantDetail")
    suspend fun getPlants(
    ): Response<PlantDetailResponse>

    @GET("plantDetail/{id}")
    suspend fun getDetailPlant(
        @Path("id") id: String,
    ): Response<PlantDetailResponse>
}
