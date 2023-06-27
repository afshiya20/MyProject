package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myproject.databinding.ActivityFifthPgBinding

class account_ready_pg : AppCompatActivity() {

    private lateinit var btnComplete : Button
    private lateinit var binding: ActivityFifthPgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_ready_pg)

        btnComplete= findViewById(R.id.btnComplete)

        btnComplete.setOnClickListener {
            val intent = Intent(this,Sixth_Pg_salary::class.java)
            startActivity(intent)
        }


    }
}