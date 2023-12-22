package com.example.florapedia.data.remote

import com.example.florapedia.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelApiConfig {
    fun getApiService(): ModelApiService {
        val baseURLModel = BuildConfig.URL_MODEL

        val loggingInterceptor =
            if(BuildConfig.DEBUG)
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURLModel)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ModelApiService::class.java)
    }
}

