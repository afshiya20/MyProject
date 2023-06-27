package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build

import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.input.key.Key.Companion.I


import com.example.myproject.databinding.ActivityEighthPgAddrBinding

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

import java.util.Locale
import java.util.Objects

class Eighth_Pg_addr : AppCompatActivity() {
    private lateinit var binding: ActivityEighthPgAddrBinding
    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var btnPlay : Button

    var mediaPlayer : MediaPlayer? = null
    var click=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eighth_pg_addr)

        binding= ActivityEighthPgAddrBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        /*----------------------------Audio Play Button-------------------------------------------*/



        this.btnPlay = findViewById(R.id.btnPlay)

        btnPlay.setOnClickListener {
            if(click==1)
            {
                playAudio()
                btnPlay.setBackgroundColor(resources.getColor(R.color.light_gray))
                click=0
            }
            else
            {
                click=1
                mediaPlayer!!.stop()
                btnPlay.setBackgroundColor(resources.getColor(R.color.gray))
            }
        }

        btnPlay.performClick()




        /*-------------------------------------------------------------------------------------*/
        binding.button2.setOnClickListener{

            val work_addr = binding.nameText.text.toString()
            updateDB(work_addr)
            /* GEtting The value from Shared Preferences */

            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val salary :String? = sharedPreferences.getString("salary", "")


            /*-------------------------------------------------*/

            if (salary != null) {
                if(salary.toInt() > 25000) {
                    val intent3 = Intent(this, Eligible::class.java)
                    startActivity(intent3)

                } else {
                    val intent3 = Intent(this, account_ready_pg::class.java)
                    startActivity(intent3)
                    Toast.makeText(this, "sry not eligible", Toast.LENGTH_SHORT).show()
                }
            }








        }

        binding.voiceBtn1.setOnClickListener{
            val intent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Let Us Know Your Salary")

            try{
                startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT)
            }catch (e:Exception){
                Toast.makeText(this,""+e.message,Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SPEECH_INPUT){
            if (resultCode== RESULT_OK && data!=null){
                val res : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.nameText.setText(Objects.requireNonNull(res)[0])

                binding.button2.performClick()
            }
        }
    }

    /*-------------------------------Play Audio Function-------------------------------------*/
    private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.work_addr)

        mediaPlayer = MediaPlayer()

        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer!!.setDataSource(
                audioUrl.fileDescriptor,
                audioUrl.startOffset,
                audioUrl.length
            )
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {

                binding.voiceBtn1.performClick()
            }


        }
        catch (e: IOException)
        {
            e.printStackTrace()

        }

        Toast.makeText(this,"Audio started Playing",Toast.LENGTH_SHORT).show()
    }

    /*---------------------------------------------------------------------------------------------*/

    private fun updateDB(result : String)
    {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname", "")

        database = FirebaseDatabase.getInstance().getReference("Users/$firstname")


        if (firstname != null) {

            database.child("work_addr").setValue(result).addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved data",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }
}

