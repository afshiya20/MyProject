package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class onboarding_2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)

        val nextButton = findViewById<Button>(R.id.next_btn)
        val skipButton = findViewById<Button>(R.id.skip_btn)

        nextButton.setOnClickListener {
            // Start the Onboard2Activity
            val intent = Intent(this, onboarding_3::class.java)
            startActivity(intent)
        }

        skipButton.setOnClickListener {
            // Start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}