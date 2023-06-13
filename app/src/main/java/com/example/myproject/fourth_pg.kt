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
import com.example.myproject.databinding.ActivityFourthPgBinding
import com.example.myproject.databinding.Layout2Binding
import com.google.firebase.database.DatabaseReference
import java.io.IOException
import java.util.Locale


class fourth_pg : AppCompatActivity() {
    private lateinit var binding:ActivityFourthPgBinding


    private lateinit var database : DatabaseReference
    private val REQUEST_CODE_SPEECH_INPUT = 1


    private lateinit var btnPlay : Button

    var mediaPlayer : MediaPlayer? = null
    var click=1
    private lateinit var result : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityFourthPgBinding.inflate(LayoutInflater.from(this))
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
        }
        binding.other.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "other"
        }
        binding.salaried.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "salaried"
        }
        binding.student.setOnClickListener{
            val intent = Intent(this, fifth_pg::class.java)
            startActivity(intent)
            result = "student"

        }




    }
/*-------------------------------Play Audio Function-------------------------------------*/
private fun playAudio()
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