package com.example.florapedia.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.florapedia.R
import com.example.florapedia.ui.fragment.CameraFragment
import com.example.florapedia.ui.fragment.FavoriteFragment
import com.example.florapedia.ui.fragment.HomeFragment
import com.example.florapedia.ui.fragment.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: MeowBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        addFragment(HomeFragment.newInstance())
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.baseline_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.baseline_photo_camera_24))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.baseline_favorite_border_24))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.baseline_perm_identity_24))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(HomeFragment.newInstance())
                }
                1 -> {
                    replaceFragment(CameraFragment.newInstance())
                }
                2 -> {
                    replaceFragment(FavoriteFragment.newInstance())
                }
                3 -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
                else -> {
                    replaceFragment(HomeFragment.newInstance())
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer, fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer, fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

}
