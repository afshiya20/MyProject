package com.example.myproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myproject.databinding.ActivitySeventhPgRelationshipBinding
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.Locale

class Seventh_Pg_relationship : AppCompatActivity() {


    private lateinit var binding:ActivitySeventhPgRelationshipBinding
    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var btnPlay : Button
    var mediaPlayer : MediaPlayer? = null
    var click=1
    private lateinit var result : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seventh_pg_relationship)
        binding = ActivitySeventhPgRelationshipBinding.inflate(LayoutInflater.from(this))

        /*----------------------------Audio Play Button-------------------------------------------*/

        btnPlay = findViewById(R.id.btnPlay)

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

        binding.voiceBtn1.setOnClickListener{
            val intent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Your Name")

            try{
                startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT)
            }catch (e:Exception){
                Toast.makeText(this,""+e.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.single.setOnClickListener{
            val intent = Intent(this, Eighth_Pg_addr::class.java)
            startActivity(intent)
            result = "single"
            updateDB(result)
        }
        binding.married.setOnClickListener{
            val intent = Intent(this, Eighth_Pg_addr::class.java)
            startActivity(intent)
            result = "married"
            updateDB(result)
        }
        binding.divorced.setOnClickListener{
            val intent = Intent(this, Eighth_Pg_addr::class.java)
            startActivity(intent)
            result = "divorced"
            updateDB(result)
        }
        binding.widowd.setOnClickListener{
            val intent = Intent(this, Eighth_Pg_addr::class.java)
            startActivity(intent)
            result = "widowd"
            updateDB(result)

        }

    }

    /* Update the database */

    private fun updateDB(result : String)
    {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname", "")

        database = FirebaseDatabase.getInstance().getReference("Users/$firstname")

        if (firstname != null) {
            val User = User( rel_status = result) // Update the dob value
            database.child("rel_status").setValue(result).addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved data",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }
    /*-------------------------------Play Audio Function-------------------------------------*/
    private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.rel_status)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SPEECH_INPUT){
            if (resultCode== RESULT_OK && data!=null){
                val res : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                res?.let {
                    result = it[0]  // Get the first result from the ArrayList
                    // Check if the result contains specific words
                    if (result.contains("single")) {
                        // Perform actions when "self" is spoken
                        binding.single.performClick()  // Trigger the click event of the button with id "btnSelf"
                    }
                    if (result.contains("married")) {
                        // Perform actions when "salaried" is spoken
                        binding.married.performClick()  // Trigger the click event of the button with id "btnSalaried"
                    }
                    if (result.contains("divorced")) {
                        // Perform actions when "student" is spoken
                        binding.divorced.performClick()  // Trigger the click event of the button with id "btnStudent"
                    }
                    if (result.contains("widowd")) {
                        // Perform actions when "other" is spoken
                        binding.widowd.performClick()  // Trigger the click event of the button with id "btnOther"
                    }

                    // Store the spoken word in the "result" string variable
                    result = it[0]
                    // ... continue with any other desired actions ...

                    Toast.makeText(this, "you selected the option "+result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}