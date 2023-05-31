package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class onboarding_3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding3)

        val getStarted = findViewById<Button>(R.id.next_btn)


        getStarted.setOnClickListener {
            // Start the Onboard2Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



    }
}