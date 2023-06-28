package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer


import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.input.key.Key.Companion.I


import com.example.myproject.databinding.ActivitySixthPgSalaryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

import java.util.Locale
import java.util.Objects

class Sixth_Pg_salary : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivitySixthPgSalaryBinding
    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var btnPlay : Button
    private lateinit var textToSpeech:TextToSpeech
    var mediaPlayer : MediaPlayer? = null
    var click=1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sixth_pg_salary)
        binding= ActivitySixthPgSalaryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        textToSpeech= TextToSpeech(this,this)

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // Audio playback started
            }

            override fun onDone(utteranceId: String?) {
                // Audio playback completed
                // Start the new activity here
                runOnUiThread {
                    binding.voiceBtn1.performClick()
                }
                // finish() // Optional: Finish the current activity if needed
            }

            override fun onError(utteranceId: String?) {
                // Error occurred during audio playback
            }
        })

        /*----------------------------Audio Play Button-------------------------------------------*/

        btnPlay = findViewById(R.id.btnPlay)



        btnPlay.setOnClickListener {
            if(click==1)
            {
                playTextToSpeech("Let's talk about money.How much do you earn in a month?")
                btnPlay.setBackgroundColor(resources.getColor(R.color.light_gray))
                click=0
            }
            else
            {
                click=1
                textToSpeech.stop()
                btnPlay.setBackgroundColor(resources.getColor(R.color.gray))
            }
        }

        btnPlay.performClick()




        /*-------------------------------------------------------------------------------------*/
        binding.button2.setOnClickListener{
            val intent3 = Intent(this, Seventh_Pg_relationship::class.java)
            startActivity(intent3)
            val salary = binding.nameText.text.toString()
            updateDB(salary)

            /* Storing the value in shared Preferences */
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("salary", salary)
            editor.apply()

            /*----------------------------------------------------------------------*/
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

    override fun onInit(status: Int) {
        if (status==TextToSpeech.SUCCESS){
            val result=textToSpeech.setLanguage(Locale.US)

            if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language not supported!")

            }else{
                playTextToSpeech("Let's talk about money.How much do you earn in a month?")
            }

        }

    }
    private fun playTextToSpeech(text:String){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")


    }
    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    /*-------------------------------Play Audio Function-------------------------------------*/
  /*  private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.salary)

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
    }*/

    /*---------------------------------------------------------------------------------------------*/

    private fun updateDB(result : String)
    {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname", "")

        database = FirebaseDatabase.getInstance().getReference("Users/$firstname")


        if (firstname != null) {

            database.child("salary").setValue(result).addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved data",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }
}

