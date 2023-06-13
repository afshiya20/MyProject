package com.example.myproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.myproject.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PREF_NAME = "MyPrefs"
    private val KEY_VOICE_JOURNEY = "voice_journey"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.button.setOnClickListener{
            val intent2 = Intent(this, secondpg::class.java)
            startActivity(intent2)

        }

        binding.button2.setOnClickListener {
            saveVoiceJourney(true)
                binding.button.performClick()
        }


    }

     fun saveVoiceJourney(value: Boolean) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_VOICE_JOURNEY, value)
        editor.apply()
    }

     fun getVoiceJourney(): Boolean {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_VOICE_JOURNEY, false)
    }
}