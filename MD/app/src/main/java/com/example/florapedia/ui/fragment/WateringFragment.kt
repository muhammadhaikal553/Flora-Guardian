package com.example.florapedia.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.florapedia.data.response.Data
import com.example.florapedia.databinding.FragmentWateringBinding
import com.example.florapedia.ui.detail.DetailViewModel
import com.google.gson.Gson

class WateringFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentWateringBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWateringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemJson = requireActivity().intent.getStringExtra("key_item")
        val gson = Gson()
        val item = gson.fromJson(itemJson, Data::class.java)

        detailViewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)

        item?.let {
            binding.tvWatering.text = it.wateringFrequence
            binding.tvMineral.text = it.waterLevel
        }

    }

    companion object {
        fun newInstance(mineral: String, watering: String): WateringFragment {
            val fragment = WateringFragment()
            val bundle = Bundle()
            bundle.putString("WATERING", watering)
            bundle.putString("MINERAL", mineral)
            fragment.arguments = bundle
            return fragment
        }
    }
}