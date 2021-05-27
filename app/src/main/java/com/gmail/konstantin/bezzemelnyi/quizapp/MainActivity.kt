package com.gmail.konstantin.bezzemelnyi.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startGameBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        startGameBtn.setOnClickListener { startGame() }
    }

    //Starts game
    private fun startGame() {
        //Creating a new Intent which takes two activities and then call start activity method
        // which resolves this intent and starts another activity

        val startGameIntent = Intent(this, QuizActivity::class.java)
        startActivity(startGameIntent)
    }

    private fun initViews() {
        startGameBtn = findViewById(R.id.main_activity_start_game_button)
    }
}