package com.example.florapedia.di

import android.app.Application
import android.content.Context
import com.example.florapedia.data.remote.ApiConfig
import com.example.florapedia.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig().getApiService()
        val application = context.applicationContext as Application

        return UserRepository.getInstance(application, apiService)
    }
}