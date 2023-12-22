package com.example.florapedia.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.florapedia.R
import com.example.florapedia.data.response.Data
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.databinding.ClassificationItemBinding
import com.example.florapedia.databinding.FavoriteItemBinding

sealed class PlantItem {
    data class Plant(val plant: Data) : PlantItem()
    data class Favorite(val favoritePlant: FavoritePlant) : PlantItem()
}

class PlantAdapter(private val onItemClick: (Any) -> Unit) : ListAdapter<PlantItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.classification_item -> {
                val binding = ClassificationItemBinding.inflate(inflater, parent, false)
                PlantViewHolder(binding)
            }
            R.layout.favorite_item -> {
                val binding = FavoriteItemBinding.inflate(inflater, parent, false)
                FavoriteViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is PlantViewHolder -> {
                (item as? PlantItem.Plant)?.let { user ->
                    holder.bindMain(user.plant)
                    holder.itemView.setOnClickListener {
                        onItemClick(user.plant)
                    }
                }
            }
            is FavoriteViewHolder -> {
                (item as? PlantItem.Favorite)?.let { favorite ->
                    holder.bindFavorite(favorite.favoritePlant)


                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PlantItem.Plant -> R.layout.classification_item
            is PlantItem.Favorite -> R.layout.favorite_item
        }
    }

    class PlantViewHolder(private val userBinding: ClassificationItemBinding) : RecyclerView.ViewHolder(userBinding.root) {
        fun bindMain(item: Data) {
            userBinding.tvName.text = item.plantFamily
            Glide.with(userBinding.root)
                .load(item.attachment)
                .into(userBinding.imgPlants)
        }
    }

    class FavoriteViewHolder(private val favoriteBinding: FavoriteItemBinding) : RecyclerView.ViewHolder(favoriteBinding.root) {
        fun bindFavorite(favoriteUser: FavoritePlant) {
            favoriteBinding.namePlant.text = favoriteUser.plantFamily
            Glide.with(favoriteBinding.root)
                .load(favoriteUser.attachment)
                .into(favoriteBinding.imagePlant)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlantItem>() {
            override fun areItemsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
                return when {
                    oldItem is PlantItem.Plant && newItem is PlantItem.Plant ->
                        oldItem.plant.id == newItem.plant.id
                    oldItem is PlantItem.Favorite && newItem is PlantItem.Favorite ->
                        oldItem.favoritePlant.plantFamily == newItem.favoritePlant.plantFamily
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}