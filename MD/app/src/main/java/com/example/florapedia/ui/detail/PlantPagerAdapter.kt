package com.example.florapedia.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.florapedia.data.response.PlantDetailResponse
import com.example.florapedia.ui.fragment.DescriptionFragment
import com.example.florapedia.ui.fragment.WateringFragment

class PlantPagerAdapter(activity: FragmentActivity, private val plantDetailResponse: PlantDetailResponse?) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val dataItem = plantDetailResponse?.data?.firstOrNull()

        return when (position) {
            0 -> DescriptionFragment.newInstance(dataItem?.description ?: "")
            1 -> WateringFragment.newInstance(dataItem?.waterLevel ?: "", dataItem?.wateringFrequence ?: "")
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

