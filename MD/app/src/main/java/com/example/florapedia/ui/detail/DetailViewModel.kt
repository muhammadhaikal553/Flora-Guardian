package com.example.florapedia.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florapedia.data.response.PlantDetailResponse
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.repository.UserRepository
import kotlinx.coroutines.launch


class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _items = MutableLiveData<PlantDetailResponse?>()
    val items: MutableLiveData<PlantDetailResponse?>
        get() = _items

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetailPlant(plantSpecies: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailPlant(plantSpecies)
                if (response.isSuccessful) {
                    _items.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            } catch (t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        }
    }

    fun insertFavoritePlant(favoritePlant: FavoritePlant) {
        repository.insert(favoritePlant)
        _isFavorite.postValue(true)
    }

    fun deleteFavoritePlant(favoritePlant: FavoritePlant) {
        repository.delete(favoritePlant)
        _isFavorite.postValue(false)
    }

    fun getAllPlants(): LiveData<List<FavoritePlant>>? = repository.getAllPlants()

    companion object{
        private const val TAG = "DetailViewModel"
    }

}
