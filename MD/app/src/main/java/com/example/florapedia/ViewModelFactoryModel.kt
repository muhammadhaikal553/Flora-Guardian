package com.example.florapedia

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.florapedia.di.InjectionModel
import com.example.florapedia.repository.PredictionRepository
import com.example.florapedia.ui.fragment.CameraViewModel


class ViewModelFactoryModel(private val repository: PredictionRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryModel? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryModel {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryModel::class.java) {
                    INSTANCE = ViewModelFactoryModel(InjectionModel.provideRepository())
                }
            }
            return INSTANCE as ViewModelFactoryModel
        }
    }
}