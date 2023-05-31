package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.myproject.databinding.ActivityFifthPgBinding



class fifth_pg : AppCompatActivity() {
    private lateinit var binding: ActivityFifthPgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFifthPgBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.button.setOnClickListener{
            val intent = Intent(this, waiting_pg::class.java)
            startActivity(intent)
        }
    }
}