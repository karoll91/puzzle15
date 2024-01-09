package com.example.puzzlegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.puzzlegame.databinding.ActivityMainBinding
import com.example.puzzlegame.ui.GameActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            llBtnExit.setOnClickListener{finishAffinity()}
        }
    }

    fun onStatic(view: View){
//        val intent = Intent(this, StaticActivity::class.java)
//        startActivity(intent)
    }

    fun onStartClicked(view: View){
        val player = binding.etPlayer.text.toString()
        if (player.isNotEmpty()){
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("player", player)
            startActivity(intent)
        }else{
//            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            binding.etPlayer.error="Please enter your username"
        }

    }
    private fun navigate(destination: Class<*>){
        startActivity(Intent(this@MainActivity, destination))
    }
}