package com.example.florapedia.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.florapedia.ViewModelFactory
import com.example.florapedia.data.adapter.PlantAdapter
import com.example.florapedia.data.adapter.PlantItem
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.databinding.FragmentFavoriteBinding
import com.example.florapedia.ui.detail.DetailActivity
import com.example.florapedia.ui.favorite.FavoriteViewModel
import com.google.gson.Gson

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var plantAdapter: PlantAdapter
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavoritePlants()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavourites.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.recyclerFavourites.addItemDecoration(itemDecoration)

        plantAdapter = PlantAdapter { item ->
            val gson = Gson()
            val itemJson = gson.toJson(item)

            val favoriteIntent = Intent(requireContext(), DetailActivity::class.java)
            favoriteIntent.putExtra(FavoritePlant.EXTRA_FAVORITE, itemJson)

            startActivity(favoriteIntent)
        }

        binding.recyclerFavourites.adapter = plantAdapter
    }

    private fun observeFavoritePlants() {
        favoriteViewModel.getFavoritePlants().observe(viewLifecycleOwner) { plants ->
            val items = arrayListOf<PlantItem>()
            plants.map {
                val item = PlantItem.Favorite(it)
                items.add(item)
            }
            plantAdapter.submitList(items)
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
