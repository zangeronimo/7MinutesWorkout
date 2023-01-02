package br.com.tudolinux.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import br.com.tudolinux.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        setupRestView()
    }

    private fun setRestProgressBar(){
        restTimer = object: CountDownTimer((restProgress * 1003).toLong(), 1000){
            override fun onTick(p0: Long) {
                restProgress--
                binding?.progressBar?.progress = restProgress
                binding?.tvTimer?.text = "$restProgress"
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        exerciseTimer = object: CountDownTimer((exerciseProgress * 1005).toLong(), 1000){
            override fun onTick(p0: Long) {
                exerciseProgress--
                binding?.progressBarExercise?.progress = exerciseProgress
                binding?.tvTimerExercise?.text = "$exerciseProgress"

                if(exerciseProgress <= 5){
                    speakOut(exerciseProgress.toString())
                }
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1) {
                    setupRestView()
                } else {
                    onBackPressed()
                }
            }
        }.start()
    }

    private fun setupRestView(){
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.text = "GET READY TO"
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvNextExercise?.text = exerciseList!![currentExercisePosition+1].getName()
        binding?.tvNextExercise?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.GONE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 10
        }

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.GONE
        binding?.tvUpcomingExercise?.visibility = View.GONE
        binding?.tvNextExercise?.visibility = View.GONE
        binding?.tvTitle?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 30
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 10
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 30
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        currentExercisePosition = -1
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported")
            }
        } else {
            Log.e("TTS","Fail to initiate TextToSpeech")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}