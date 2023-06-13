package com.example.myproject

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.example.myproject.databinding.ActivityFifthPgBinding

import com.google.firebase.database.DatabaseReference
import java.io.IOException
import java.util.Locale
import java.util.Objects


class fifth_pg : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1


    private lateinit var btnPlay : Button

    var mediaPlayer : MediaPlayer? = null
    var click=1
    private lateinit var binding: ActivityFifthPgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFifthPgBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

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



        binding.button.setOnClickListener{
            val intent = Intent(this, waiting_pg::class.java)
            startActivity(intent)
        }

        /*---------------------Voice Button---------------------------------------------*/
        binding.button.setOnClickListener{
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

        /*--------------------------------------------------------------------------------------*/
    }

    /*-------------------------------Play Audio Function-------------------------------------*/
    private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.pan_page_)

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

                binding.button.performClick()
            }


        }
        catch (e: IOException)
        {
            e.printStackTrace()

        }

        Toast.makeText(this,"Audio started Playing",Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SPEECH_INPUT){
            if (resultCode== RESULT_OK && data!=null){
                val res : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.pan.setText(Objects.requireNonNull(res)[0])
            }
        }
    }

    /*---------------------------------------------------------------------------------------------*/
}