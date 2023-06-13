package com.example.myproject

import android.app.DatePickerDialog
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.Layout2Binding
import com.example.myproject.databinding.Layout3Binding
import com.google.firebase.database.DatabaseReference
import java.io.IOException
import java.time.Year
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class thirdpg : AppCompatActivity() {
    private lateinit var binding: Layout3Binding
    private val REQUEST_CODE_SPEECH_INPUT = 1


    private lateinit var database : DatabaseReference
    private lateinit var btnPlay : Button

    var mediaPlayer : MediaPlayer? = null
    var click=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Layout3Binding.inflate(LayoutInflater.from(this))
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


        binding.button3.setOnClickListener{
            val intent3 = Intent(this, fourth_pg::class.java)
            startActivity(intent3)
        }
        val button = findViewById<Button>(R.id.dateBtn)
        val text=findViewById<TextView>(R.id.dateText)
        val cal = Calendar.getInstance()
        val myYear =cal.get(Calendar.YEAR)
        val myMonth=cal.get(Calendar.MONTH)
        val day =cal.get(Calendar.DAY_OF_MONTH)

        button.setOnClickListener{
            val datePickerDialog =DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view,year,month,dayOfMonth ->
                text.text= "" +dayOfMonth + "/ " +(month+1) + "/ "+year

            },myYear,myMonth,day)
            datePickerDialog.show()
        }
        binding.voiceBtn2.setOnClickListener{
            val intent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to Text")

            try{
                startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT)
            }catch (e:Exception){
                Toast.makeText(this,""+e.message, Toast.LENGTH_LONG).show()

            }
        }

        binding.voiceBtn2.setOnClickListener{
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
                binding.dateText.setText(Objects.requireNonNull(res)[0])
                binding.button3.performClick()
            }
        }
    }

    /*-------------------------------Play Audio Function-------------------------------------*/
    private fun playAudio()
    {
        val audioUrl = resources.openRawResourceFd(R.raw.dob_page)

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

                binding.voiceBtn2.performClick()
            }


        }
        catch (e: IOException)
        {
            e.printStackTrace()

        }

        Toast.makeText(this,"Audio started Playing",Toast.LENGTH_SHORT).show()
    }

    /*---------------------------------------------------------------------------------------------*/


}