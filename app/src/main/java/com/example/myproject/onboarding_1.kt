package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button

class onboarding_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding1)


        val nextButton = findViewById<Button>(R.id.next_btn)
        val skipButton = findViewById<Button>(R.id.skip_btn)

        nextButton.setOnClickListener {
            // Start the Onboard2Activity
            val intent = Intent(this, onboarding_2::class.java)
            startActivity(intent)
        }

        skipButton.setOnClickListener {
            // Start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}