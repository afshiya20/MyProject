package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.example.myproject.databinding.ActivityWaitingPgBinding

class waiting_pg : AppCompatActivity() {
    private lateinit var binding: ActivityWaitingPgBinding
    private val SPLASH_DELAY: Long = 3000 // 3 seconds delay
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_pg)


        Handler().postDelayed({
            val intent = Intent(this, account_ready_pg::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}