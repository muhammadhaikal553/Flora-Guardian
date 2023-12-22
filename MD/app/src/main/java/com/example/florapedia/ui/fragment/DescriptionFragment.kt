package com.example.florapedia.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.florapedia.data.response.Data
import com.example.florapedia.databinding.FragmentDescriptionBinding
import com.example.florapedia.ui.detail.DetailViewModel
import com.google.gson.Gson

class DescriptionFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemJson = requireActivity().intent.getStringExtra("key_item")
        val gson = Gson()
        val item = gson.fromJson(itemJson, Data::class.java)

        detailViewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)

        item?.let {
            binding.tvDescription.text = it.description
        }
    }

    companion object {
        fun newInstance(description: String): DescriptionFragment {
            val fragment = DescriptionFragment()
            val bundle = Bundle()
            bundle.putString("DESCRIPTION", description)
            fragment.arguments = bundle
            return fragment
        }
    }
}

