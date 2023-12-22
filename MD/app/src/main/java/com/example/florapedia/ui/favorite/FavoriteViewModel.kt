package com.example.florapedia.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.repository.UserRepository

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    fun getFavoritePlants(): LiveData<List<FavoritePlant>> {
        return repository.getAllPlants()?.map { favoritePlant ->
            favoritePlant?.map {
                FavoritePlant(plantFamily = it.plantFamily, attachment = it.attachment)
            } ?: emptyList()
        } ?: MutableLiveData(emptyList())
    }
}