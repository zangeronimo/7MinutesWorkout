package br.com.tudolinux.a7minutesworkout

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

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
                Toast.makeText(this@ExerciseActivity,"timer finished", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun setupRestView(){
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 10
        }

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.GONE
        binding?.tvTitle?.text = "EXERCISE NAME"
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

        binding = null
    }
}