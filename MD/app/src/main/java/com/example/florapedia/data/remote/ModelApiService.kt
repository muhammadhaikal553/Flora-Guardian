package com.example.florapedia.data.remote

import com.example.florapedia.data.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ModelApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<PredictResponse>
}
