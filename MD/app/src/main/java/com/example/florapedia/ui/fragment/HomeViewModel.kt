package com.example.florapedia.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florapedia.data.adapter.PlantItem
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _items = MutableLiveData<List<PlantItem?>>()
    val items: LiveData<List<PlantItem?>> = _items

    fun getPlants() {
        viewModelScope.launch {
            try {
                val response = repository.getPlants()
                if (response.isSuccessful) {
                    val plants = response.body()?.data ?: emptyList()
                    _items.postValue(plants.map { PlantItem.Plant(it) })
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            } catch (t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        }
    }

    fun getFavoritePlants(): LiveData<List<FavoritePlant>>? {
        return repository.getFavoritePlants()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
