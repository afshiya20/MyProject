package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class splash : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000 // 3 seconds delay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, onboarding_1::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}