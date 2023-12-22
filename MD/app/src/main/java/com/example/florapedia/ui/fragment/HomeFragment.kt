package com.example.florapedia.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.florapedia.R
import com.example.florapedia.ViewModelFactory
import com.example.florapedia.data.adapter.PlantAdapter
import com.example.florapedia.data.adapter.PlantItem
import com.example.florapedia.data.response.Data
import com.example.florapedia.database.FavoritePlant
import com.example.florapedia.databinding.FavoriteItemBinding
import com.example.florapedia.ui.detail.DetailActivity
import com.example.florapedia.ui.welcome.WelcomeActivity
import com.google.gson.Gson

class HomeFragment : Fragment() {
    private lateinit var classificationAdapter: PlantAdapter
    private lateinit var favoriteAdapter: PlantAdapter
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var rvClassification: RecyclerView
    private lateinit var rvFavorite: RecyclerView
    private lateinit var emptyFavoriteText: TextView
    private lateinit var searchView: SearchView
    private var allPlants: List<PlantItem> = emptyList()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_topbar, container, false)
        setHasOptionsMenu(true)

        rvClassification = rootView.findViewById(R.id.rv_classification)
        rvFavorite = rootView.findViewById(R.id.rv_favorite)
        emptyFavoriteText = rootView.findViewById(R.id.emptyFavoriteText)

        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    navigateToWelcomeScreen()
                    true
                }
                else -> false
            }
        }

        val classificationLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val favoriteLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rvClassification.layoutManager = classificationLayoutManager
        rvFavorite.layoutManager = favoriteLayoutManager

        classificationAdapter = PlantAdapter { item ->
            if (item is Data) {
                val gson = Gson()
                val itemJson = gson.toJson(item)

                val detailIntent = Intent(requireContext(), DetailActivity::class.java)
                detailIntent.putExtra("key_item", itemJson)
                startActivity(detailIntent)
            }
        }

         favoriteAdapter = PlantAdapter { item ->
            if (item is FavoritePlant) {
                val favoriteBinding: FavoriteItemBinding = FavoriteItemBinding.inflate(layoutInflater)
                val favoritePlantDetailsTextView: TextView = favoriteBinding.namePlant
                val favoritePlantDetailsImageView: ImageView = favoriteBinding.imagePlant

                item.let { favorite ->
                    favoritePlantDetailsTextView.text = favorite.plantFamily
                    Glide.with(requireContext())
                        .load(favorite.attachment)
                        .into(favoritePlantDetailsImageView)
                }
            }
        }

        rvClassification.adapter = classificationAdapter
        rvFavorite.adapter = favoriteAdapter

        homeViewModel.items.observe(viewLifecycleOwner) { items ->
            allPlants = items as List<PlantItem>
            classificationAdapter.submitList(allPlants)
        }

        // Initialize SearchView
        searchView = rootView.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPlants(newText.orEmpty())
                return true
            }
        })

        setupRecyclerView()

        return rootView
    }

    private fun filterPlants(query: String) {
        val filteredPlants = if (query.isEmpty()) {
            allPlants
        } else {
            allPlants.filter { plant ->
                when (plant) {
                    is PlantItem -> {
                        (plant as? PlantItem.Plant)?.plant?.plantFamily?.contains(query, ignoreCase = true) == true
                    }
                }
            }
        }
        classificationAdapter.submitList(filteredPlants)
    }

    private fun mapFavoritePlantsToPlantItems(favoritePlants: List<FavoritePlant>): List<PlantItem> {
        return favoritePlants.map { PlantItem.Favorite(it) }
    }

    private fun setupRecyclerView() {
        homeViewModel.getPlants()

        homeViewModel.items.observe(viewLifecycleOwner) { items ->
            classificationAdapter.submitList(items)
        }

        homeViewModel.getFavoritePlants()?.observe(viewLifecycleOwner) { favoritePlants ->
            val plantItems = mapFavoritePlantsToPlantItems(favoritePlants)
            favoriteAdapter.submitList(plantItems)
            if (plantItems.isEmpty()) {
                emptyFavoriteText.visibility = View.VISIBLE
            } else {
                emptyFavoriteText.visibility = View.GONE
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun navigateToWelcomeScreen() {
        val welcomeIntent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(welcomeIntent)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}