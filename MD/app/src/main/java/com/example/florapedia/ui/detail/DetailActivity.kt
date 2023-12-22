package com.example.florapedia.ui.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.florapedia.R
import com.example.florapedia.ViewModelFactory
import com.example.florapedia.data.response.Data
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemJson = intent.getStringExtra("key_item")
        val gson = Gson()
        val item = gson.fromJson(itemJson, Data::class.java)

        val favoriteItemJson = intent.getStringExtra(FavoritePlant.EXTRA_FAVORITE)
        val gsonFavorite = Gson()
        val favoriteItem = gsonFavorite.fromJson(favoriteItemJson, FavoritePlant::class.java)


        initializeUI(item, favoriteItem)
        setFavoriteButtonBehavior(item, favoriteItem)

    }

    private fun initializeUI(item: Data?, favoriteItem: FavoritePlant?) {
        item?.let {
            binding.tvFamily.text = it.plantFamily
            Glide.with(this)
                .load(it.attachment)
                .into(binding.imagePlant)
        }

        val plantDetail = item?.plantFamily
        val plantFromFavorite = favoriteItem?.plantFamily
        val plant = plantDetail ?: plantFromFavorite ?: ""

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)

        detailViewModel.items.observe(this, Observer { detailStoryResponse ->
            detailStoryResponse?.let { detailData ->

                val viewPagerAdapter = PlantPagerAdapter(this, detailData)
                viewPager.adapter = viewPagerAdapter

                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "Description"
                            viewPager.currentItem = 0
                        }
                        1 -> tab.text = "Watering"
                    }
                }.attach()

                showLoading(false)

            }
        })

        favoriteItem?.let { favorite ->
            binding.tvFamily.text = favorite.plantFamily

            Glide.with(this)
                .load(favorite.attachment)
                .into(binding.imagePlant)
        }

        plant.let {
            showLoading(true)
            detailViewModel.getDetailPlant(it)
        }
    }

    private fun setFavoriteButtonBehavior(item: Data?, favoriteItem: FavoritePlant?) {
        detailViewModel.getAllPlants()?.observe(this) { favoriteUsers ->
            val plant = item?.plantFamily
            val plantFavorite = favoriteItem?.plantFamily
            val isUserInDatabase = favoriteUsers.any { it.plantFamily == plant || it.plantFamily == plantFavorite }

            if (isUserInDatabase) {
                binding.favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.brown))
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)

                binding.favoriteButton.setOnClickListener {
                    val favoriteUserDelete1 = FavoritePlant(
                        plant ?: "",
                        item?.attachment ?: ""
                    )

                    val favoriteUserDelete2 = FavoritePlant(
                        plantFavorite ?: "",
                        favoriteItem?.attachment ?: ""
                    )

                    detailViewModel.deleteFavoritePlant(favoriteUserDelete1)
                    detailViewModel.deleteFavoritePlant(favoriteUserDelete2)

                    binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
                }

            } else {
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)

                binding.favoriteButton.setOnClickListener {
                    val favoriteUserInsert1 = FavoritePlant(
                        plant ?: "",
                        item?.attachment
                            ?: ""
                    )

                    val favoriteUserInsert2 = FavoritePlant(
                        plantFavorite
                            ?: "",
                        favoriteItem?.attachment
                            ?: ""
                    )

                    if (plant != null) {
                        detailViewModel.insertFavoritePlant(favoriteUserInsert1)
                    }

                    if (plantFavorite != null) {
                        detailViewModel.insertFavoritePlant(favoriteUserInsert2)
                    }

                    binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

