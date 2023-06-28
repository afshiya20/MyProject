package com.example.myproject

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.example.myproject.databinding.ActivityFourthPgBinding
import com.example.myproject.databinding.Layout2Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.Locale


class fourth_pg : AppCompatActivity() , TextToSpeech.OnInitListener {
    private lateinit var binding:ActivityFourthPgBinding
    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var btnPlay : Button
    private lateinit var textToSpeech:TextToSpeech
    var click=1
    private lateinit var result : String
  //  var mediaPlayer : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityFourthPgBinding.inflate(LayoutInflater.from(this))
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
                playTextToSpeech("Great!What's your employment type?How are you utilizing your time?")
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




        binding.self.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "self"
            updateDB(result)
        }
        binding.other.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "other"
            updateDB(result)
        }
        binding.salaried.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "salaried"
            updateDB(result)
        }
        binding.student.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "student"
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
            val User = User( emp_type = result) // Update the dob value
            database.child("emp_type").setValue(result).addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved data",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }
/*-------------------------------Play Audio Function-------------------------------------*/
/*private fun playAudio()
{
    val audioUrl = resources.openRawResourceFd(R.raw.employment_page)

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

 */
    override fun onInit(status: Int) {
        if (status==TextToSpeech.SUCCESS){
            val result=textToSpeech.setLanguage(Locale.US)

            if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language not supported!")

            }else{
                playTextToSpeech("Great!What's your employment type?How are you utilizing your time?")
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

/*---------------------------------------------------------------------------------------------*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SPEECH_INPUT){
            if (resultCode== RESULT_OK && data!=null){
                val res : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                res?.let {
                     result = it[0]  // Get the first result from the ArrayList
                    // Check if the result contains specific words
                    if (result.contains("self")) {
                        // Perform actions when "self" is spoken
                        binding.self.performClick()  // Trigger the click event of the button with id "btnSelf"
                    }
                    if (result.contains("salaried")) {
                        // Perform actions when "salaried" is spoken
                        binding.salaried.performClick()  // Trigger the click event of the button with id "btnSalaried"
                    }
                    if (result.contains("student")) {
                        // Perform actions when "student" is spoken
                        binding.student.performClick()  // Trigger the click event of the button with id "btnStudent"
                    }
                    if (result.contains("other")) {
                        // Perform actions when "other" is spoken
                        binding.other.performClick()  // Trigger the click event of the button with id "btnOther"
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