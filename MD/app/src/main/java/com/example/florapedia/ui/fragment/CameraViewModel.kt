package com.example.florapedia.ui.fragment

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florapedia.data.response.PredictResponse
import com.example.florapedia.repository.PredictionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraViewModel(private val repository: PredictionRepository) : ViewModel() {
    val predictResult = MutableLiveData<PredictResponse>()

    fun predictModel(image: Bitmap) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val response = repository.predictModel(image)

                if (response.isSuccessful) {
                    predictResult.value = response.body()
                } else {
                    println("Error: ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


