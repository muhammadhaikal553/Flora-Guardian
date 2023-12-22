package com.example.florapedia.repository

import android.graphics.Bitmap
import com.example.florapedia.data.remote.ModelApiService
import com.example.florapedia.data.response.PredictResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

class PredictionRepository private constructor(
    private val modelApiService: ModelApiService
) {

    suspend fun predictModel(image: Bitmap): Response<PredictResponse> {
        val requestBody = createRequestBodyFromBitmap(image)
        val filePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
        return modelApiService.uploadImage(filePart)
    }

    private fun createRequestBodyFromBitmap(bitmap: Bitmap): RequestBody {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return RequestBody.create("image/*".toMediaTypeOrNull(), byteArray)
    }

    companion object {
        @Volatile
        private var INSTANCE: PredictionRepository? = null

        fun getInstance(modelApiService: ModelApiService): PredictionRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PredictionRepository(modelApiService)
                    .also { INSTANCE = it }
            }
        }
    }
}
