package com.example.puzzlegame.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.puzzlegame.MainActivity
import com.example.puzzlegame.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            llBtnHomePage.setOnClickListener{navigate(MainActivity::class.java)}
            llBtnExit.setOnClickListener{finishAffinity()}
        }

        setStepResults()
    }

    fun onStartClicked(view: View){
        val player = binding.etPlayer.text.toString()
        if (player.isNotEmpty()){
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("player", player)
            startActivity(intent)
            finish()
        }else{
//            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            binding.etPlayer.error="Please enter your username"
        }
    }

    private fun setStepResults(){
        val countStep = intent.getStringExtra("countstep")
        val timer = intent.getStringExtra("timer")
        binding.tvSteps.text = countStep
        binding.tvTime.text = timer
    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigate(destination: Class<*>){
        startActivity(Intent(this@GameOverActivity, destination))
        finish()
    }


}