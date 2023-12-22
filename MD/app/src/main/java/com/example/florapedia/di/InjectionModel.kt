package com.example.florapedia.di

import com.example.florapedia.data.remote.ModelApiConfig
import com.example.florapedia.repository.PredictionRepository

object InjectionModel {
    fun provideRepository(): PredictionRepository {
        val modelApiService = ModelApiConfig().getApiService()

        return PredictionRepository.getInstance(modelApiService)
    }
}