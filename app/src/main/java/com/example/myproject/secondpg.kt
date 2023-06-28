package com.example.myproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityMainBinding
import com.example.myproject.databinding.Layout2Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.io.IOException

import java.util.Locale
import java.util.Objects




class secondpg : AppCompatActivity(),OnInitListener{
    private lateinit var binding: Layout2Binding
    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var btnPlay : Button
    private lateinit var textToSpeech:TextToSpeech
    var click=1
// var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= Layout2Binding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        textToSpeech= TextToSpeech(this,this)

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) {
                // Audio playback started
            }

            override fun onDone(utteranceId: String?) {
                // Audio playback completed
                runOnUiThread {
                    binding.voiceBtn1.performClick()
                }
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
                playTextToSpeech("Let's get started.Could you tell me your name?Ensure correct name is being provided.You won't be able to change it later")
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
            val firstname = binding.nameText.text.toString()
            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(firstname = firstname)

            database.child(firstname).setValue(User).addOnSuccessListener {
                Toast.makeText(this,"Successfully Saved data",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }

            /* Storing the value in shared Preferences */
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("firstname", firstname)
            editor.apply()

            /*----------------------------------------------------------------------*/


            val intent3 = Intent(this, thirdpg::class.java)
            startActivity(intent3)

        }

        binding.voiceBtn1.setOnClickListener{
            val intent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Your Name")

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
                playTextToSpeech("Let's get started.Could you tell me your name?Ensure correct name is being provided.You won't be able to change it later")
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
   /* private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.name_page)

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
*/
}