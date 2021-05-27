package com.e.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirm.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    // for using text to speech we need to inherit TexttoSpeech.OnInitListener and also
    //implement its members

    private var restTimer : CountDownTimer? = null
    private var restProgress =0

    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress =0

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePostion =-1

    private var tts:TextToSpeech? = null
    private var player:MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionBar= supportActionBar
        //making object of action bar
        if(actionBar!=null)
        {
                actionBar.setDisplayHomeAsUpEnabled(true)
                // this will set the back button on action bar
        }
        toolbar_exercise_activity.setNavigationOnClickListener {

            customDialogForBackButton()
        }

        setupRestView()

        tts = TextToSpeech(this,this)
        //initialing tts

        exerciseList = Constants.defaultExerciseList()

        setupExerciseStatusRecyclerView()

    }


    override fun onBackPressed() {
        //todo : fixed it
        customDialogForBackButton()
        //super.onBackPressed()
    }

    override fun onDestroy() {

        // close the timer also it is like closing the file

        if(restTimer!=null)
        {
            restTimer!!.cancel()
            restProgress=0
        }

        if(exerciseTimer!=null)
        {
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        if(tts!=null)
        {
            tts!!.stop()
            tts!!.shutdown()
        }
        //if tts is still speaking while we close the application it will shut it down

        if(player!=null)
        {
            player!!.stop()
        }

        super.onDestroy()
    }



    private fun setupRestView()
    {

        llRestView.visibility=View.VISIBLE
        llExerciseView.visibility=View.GONE


        /*OLD WAY
        val soundURI = Uri.parse("android:resource://e.a7minutesworkout/"
        player = MediaPlayer.create(applicationContext,soundURI)
         ////soundURI is the location where we can find the audio

        //NEW WAY
        use R.raw.press_start
        // made a directory/folder raw and paste image
        */
        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            //is this property "islooping" is true then it will constantly play
            // this sound over and over again
            player!!.start()
        }catch (e:Exception) {
            e.printStackTrace()

        }

        if(restTimer!=null)
        {
            restTimer!!.cancel()
            restProgress=0
        }


        if(currentExercisePostion > -1)
        {
            tvRestView.text = ("Next exercise will begin in ").toString()
        }

        try {

            tvUpcomingExerciseName.text = exerciseList!![currentExercisePostion+1].getName()
        }catch (e:Exception)
        {

            /*Toast.makeText(this@ExerciseActivity,
                "Not working",
                Toast.LENGTH_SHORT).show()*/
            tvUpcomingExerciseName.text = "Jumping Jacks"

        }
        setRestProgressBar()

    }

    private fun setRestProgressBar()
    {
        progressBar.progress=restProgress
        restTimer = object: CountDownTimer(10000,1000){

            //onTick is run after every interval here after 1000ms or 1 sec
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10-restProgress
                tvTimer.text = (10-restProgress).toString()
            }

            override fun onFinish() {

                currentExercisePostion++

                exerciseList!![currentExercisePostion].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged() // whenever we change something in data we have to tell the adapter
                // so it will change the view or recalculate

                setupExerciseView()
            }
        }.start()


        //CountDownTimer has attributes/features like .start() and .cancel()


    }

    private fun setExerciseProgressBar()
    {
        ExerciseprogressBar.progress=exerciseProgress
        exerciseTimer = object: CountDownTimer(30000,1000){

            //onTick is run after every interval here after 1000ms or 1 sec
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                ExerciseprogressBar.progress = 30-exerciseProgress
                tvExerciseTimer.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {
                //todo :: play with "?" and "!!"
                if(currentExercisePostion < exerciseList?.size!!-1)
                {
                    exerciseList!![currentExercisePostion].setIsSelected(false)
                    exerciseList!![currentExercisePostion].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }
                else{

                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                    finish()

                }



            }
        }.start()

        //CountDownTimer has attributes/features like .start() and .cancel()
    }

    private fun setupExerciseView()
    {

        //todo very imp changing visibilty
        llRestView.visibility=View.GONE
        llExerciseView.visibility=View.VISIBLE

        if(exerciseTimer!=null)
        {
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        speakOut(exerciseList!![currentExercisePostion].getName())

        setExerciseProgressBar()


        ivImage.setImageResource(exerciseList!![currentExercisePostion].getImage())
        tvExerciseName.text=exerciseList!![currentExercisePostion].getName()

    }

    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.UK)
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED ){
                Log.e("TTS","The language specified is not supported!")
            }
        }else{
            Log.e("TTS","The initialisation failed!")

        }
    }

    private fun speakOut(text :String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")

        //QUEUE_FLUSH :: when we press the button again without
        // waiting the prev one to complete it adds in queue(QUEUE_ADD) to remove that we use this
        //it will start speaking again
    }

    private fun setupExerciseStatusRecyclerView() {

        // Defining a layout manager for the recycle view
        // Here we have used a LinearLayout Manager with horizontal scroll.
        rvExerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // As the adapter expects the exercises list and context so initialize it passing it.
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)

        // Adapter class is attached to recycler view
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){

        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_back_confirm)

        customDialog.tvYes.setOnClickListener {
            finish() // we could use intent but it will be in stack
            // and we can go to it by pressing back so finish() is better
            customDialog.dismiss()
        }

        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

}
