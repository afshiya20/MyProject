package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.myproject.databinding.ActivityFourthPgBinding



class fourth_pg : AppCompatActivity() {
    private lateinit var binding:ActivityFourthPgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityFourthPgBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.self.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
        }
        binding.other.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
        }
        binding.salaried.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
        }
        binding.student.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
        }


    }
}