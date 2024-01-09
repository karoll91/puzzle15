package com.example.puzzlegame.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.puzzlegame.R
import com.example.puzzlegame.databinding.ActivityGameBinding
import kotlin.math.abs

class GameActivity : AppCompatActivity() {

    //Timers
    private lateinit var btnPlay: GridLayout
    private lateinit var btnPause: LinearLayout
    private lateinit var chronometer: Chronometer

    lateinit var binding: ActivityGameBinding
    lateinit var emptyButton: Button

    var player: String? = null
    val numbers: MutableList<Int> = ArrayList()
    var isPlay = false
    var pauseOffset: Long = 0
    var x: Int = 0
    var y: Int = 0
    var countStep = 0
    var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnPlay = binding.glGrid
        btnPause = binding.btnPause
        chronometer = binding.chronometerTimer

        setPlayerName()
        initNumbers()
        generateNumbers()
        updateGridUi()
        startTimer()
    }

    private fun startTimer() {
        chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
        chronometer.start()
        isPlay = true
    }

    private fun pauseTimer() {
        btnPause.setOnClickListener {
            if (isPlay) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                isPlay = false
                binding.ivPausePlay.setImageResource(R.drawable.ic_play)
                binding.tvPauseStart.setText("Play game")
            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                binding.ivPausePlay.setImageResource(R.drawable.ic_pause)
                binding.tvPauseStart.setText("Pause game")
                isPlay = true
            }
        }
    }

    fun pauseGame(view: View) {
        pauseTimer()
    }

    fun onCellClicked(view: View) {
//        Toast.makeText(this, view.tag.toString(), Toast.LENGTH_SHORT).show()
        var counter = 1
        val clicked = view as Button

        val tag = view.tag.toString()
        val clickedX = tag[0].digitToInt()
        val clickedY = tag[1].digitToInt()

//      Toast.makeText(this, "$clickedX - $clickedY", Toast.LENGTH_SHORT).show()
        if (abs((clickedX + clickedY) - (x + y)) == 1 && abs(clickedX - x) != 2 && abs(clickedY - y) != 2) {
            countWalks()

            emptyButton.visibility = View.VISIBLE
            clicked.visibility = View.INVISIBLE

            val text = clicked.text.toString()
            clicked.text = ""
            emptyButton.text = text
            emptyButton = clicked

            x = clickedX
            y = clickedY

            for (i in 0 until 15) {
                val button = binding.glGrid.getChildAt(i) as Button

                if (button.text.isEmpty()) break

                if (button.text.toString().toInt() != counter) {
                    break
                } else {
                    counter++
                }

                if (counter == 16) {
//                    Toast.makeText(this, "You won the game", Toast.LENGTH_SHORT).show()
                    isGameOver = true
                    val step = binding.tvSteps.text.toString()
                    val timer = binding.chronometerTimer.text.toString()
                    val intent = Intent(this, GameOverActivity::class.java)
                    intent.putExtra("countstep", step)
                    intent.putExtra("timer",timer)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun countWalks() {
        binding.tvSteps.text = (++countStep).toString()
        if (!isPlay) {
            startTimer()
            binding.ivPausePlay.setImageResource(R.drawable.ic_pause)
            binding.tvPauseStart.setText("Pause game")
            isPlay = true
        }
    }

    fun restartGame(view: View) {
        emptyButton.visibility = View.VISIBLE
        shuffleGrid()
        updateGridUi()
        countStep = 1
        binding.tvSteps.text = countStep.toString()
        startTimer()
        if (isPlay) {
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
        }
    }

    fun initNumbers() {
        for (i in 1..16) {
            numbers.add(i)
        }
    }

    fun generateNumbers() {
        do {
            shuffleGrid()
        } while (!isSolvable(numbers))
    }

    fun updateGridUi() {
        for (i in 0 until binding.glGrid.childCount) {
            if (numbers[i] == 16) {
                val tag = binding.glGrid.getChildAt(i).tag.toString()
                x = tag[0].digitToInt()
                y = tag[1].digitToInt()

                emptyButton = binding.glGrid.getChildAt(i) as Button
                emptyButton.visibility = View.INVISIBLE
            }
            val button = binding.glGrid.getChildAt(i) as Button
            button.text = numbers[i].toString()
        }
    }

    fun shuffleGrid() {
        numbers.shuffle()
    }

    fun setPlayerName() {
        player = intent?.getStringExtra("player")
        binding.tvPlayer.text = player
    }

    fun isSolvable(numbers: List<Int>): Boolean {
        var inversions = 0
        for (i in numbers.indices) {
            if (numbers[i] == 16) {
                inversions += i / 4 + 1
                continue
            }
            for (j in (i + 1) until numbers.size) {
                if (numbers[i] > numbers[j]) {
                    inversions++
                }
            }
        }
        return inversions % 2 == 0
    }

    fun goBack(view: View) {
        finish()
    }
}