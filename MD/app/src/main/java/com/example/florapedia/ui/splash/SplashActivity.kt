package com.example.florapedia.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.florapedia.databinding.ActivitySplashBinding
import com.example.florapedia.ui.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            if (!isFinishing) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT)

    }
}
