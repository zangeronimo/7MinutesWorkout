package br.com.tudolinux.a7minutesworkout

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.tudolinux.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private val restProgressTimerDuration = 10
    private val exerciseTimerDuration = 30

    private var binding : ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = restProgressTimerDuration

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = exerciseTimerDuration

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        tts = TextToSpeech(this, this)
        setupMediaPlayer()

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setRestProgressBar(){
        binding?.tvTimer?.text = "$restProgressTimerDuration"
        restTimer = object: CountDownTimer((restProgressTimerDuration * 1003).toLong(), 1000){
            override fun onTick(p0: Long) {
                restProgress--
                binding?.progressBar?.progress = restProgress
                binding?.tvTimer?.text = "$restProgress"

                if (restProgress == 1) {
                    speakOut("Ready?")
                }
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.tvTimerExercise?.text = "$exerciseTimerDuration"

        exerciseTimer = object: CountDownTimer((exerciseTimerDuration * 1005).toLong(), 1000){
            override fun onTick(p0: Long) {
                exerciseProgress--
                binding?.progressBarExercise?.progress = exerciseProgress
                binding?.tvTimerExercise?.text = "$exerciseProgress"

                if(exerciseProgress in 1..5){
                    speakOut(exerciseProgress.toString())
                }
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    private fun setupMediaPlayer() {
        try{
            var soundURI = Uri.parse("android.resource://br.com.tudolinux.a7minutesworkout/" + R.raw.press_start)

            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setupRestView(){

        player?.start()

        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.text = "GET READY TO"
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvNextExercise?.text = exerciseList!![currentExercisePosition+1].getName()
        binding?.tvNextExercise?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.GONE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = restProgressTimerDuration
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
            exerciseProgress = exerciseTimerDuration
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = restProgressTimerDuration
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = exerciseTimerDuration
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null) {
            player?.stop()
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