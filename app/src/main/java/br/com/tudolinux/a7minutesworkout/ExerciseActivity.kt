package br.com.tudolinux.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import br.com.tudolinux.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private var binding : ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        setupRestView()
    }

    private fun setRestProgressBar(){
        restTimer = object: CountDownTimer((restProgress * 1000).toLong(), 1000){
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
        exerciseTimer = object: CountDownTimer((exerciseProgress * 1000).toLong(), 1000){
            override fun onTick(p0: Long) {
                exerciseProgress--
                binding?.progressBarExercise?.progress = exerciseProgress
                binding?.tvTimerExercise?.text = "$exerciseProgress"
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
        binding?.tvTitle?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 30
        }

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

        currentExercisePosition = -1
        binding = null
    }
}