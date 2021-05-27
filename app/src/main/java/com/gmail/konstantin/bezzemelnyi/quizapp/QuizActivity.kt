package com.gmail.konstantin.bezzemelnyi.quizapp

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import kotlin.math.roundToInt


class QuizActivity : AppCompatActivity() {
    //Views
    private lateinit var questionTV: TextView
    private lateinit var pointsCounterTV: TextView
    private lateinit var continueBtn: Button
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var answerOneBtn: RadioButton
    private lateinit var answerTwoBtn: RadioButton
    private lateinit var answerThreeBtn: RadioButton
    private lateinit var answerFourBtn: RadioButton

    //Game objects
    private var currentGameScore = 0
    private var currentQuestionNumber: Int = 1
    private lateinit var currentGameData: quizData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        initViews()

        setupGameData()
        updateViews()

        continueBtn.setOnClickListener {
            continueGame()
        }
    }

    private fun continueGame() {
        val gameContinues = currentQuestionNumber != MAX_QUESTIONS_NUMBER

        //If game is finished yet
        if (!gameContinues) {
            finishGame()
            return
        }
        //If game continues
        if (gameContinues) {
            //If answer is not selected
            if (!answerIsSelected()) {
                Toast.makeText(
                    this,
                    getString(R.string.answer_not_selected),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            //If answer was selected and it is correct
            if (answerIsCorrect()) {
                currentGameScore++ //Add score point if the selected answer was correct
            }
            moveToNextQuestion() //After a few seconds the next question will be shown
        } else {
            finishGame() // show an alert dialog with detailed information
        }
    }

    private fun moveToNextQuestion() {
        continueBtn.isEnabled = false
        Handler().postDelayed(Runnable {
            currentGameData = Quiz(this).getQuestionData(++currentQuestionNumber)
            updateViews()
            answersRadioGroup.forEach { it ->
                if (it is RadioButton) {
                    it.reset()
                }
            }
            continueBtn.isEnabled = true
        }, NEXT_QUESTION_SHOW_DELAY)

    }

    private fun setupGameData() {
        currentGameData = Quiz(this).getQuestionData(currentQuestionNumber)
    }


    private fun finishGame() {
        AlertDialog.Builder(this)
            .setTitle(currentGameScore.toString() + getString(R.string.correct_answers_from) + MAX_QUESTIONS_NUMBER)
            .setMessage(getString(R.string.your_average_success_rank) + (currentGameScore.toDouble() / MAX_QUESTIONS_NUMBER * 100).roundToInt() + "%")
            .setPositiveButton(
                getString(R.string.restart)
            ) { _, _ ->
                startActivity(
                    Intent(
                        this,
                        this::class.java
                    )
                )
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ -> finish() }
            .setOnCancelListener { finishGame() }
            .create().show()
    }

    private fun updateViews() {
        questionTV.text = currentGameData.first
        pointsCounterTV.text = currentGameScore.toString()
        answerOneBtn.text = currentGameData.second[0].name
        answerTwoBtn.text = currentGameData.second[1].name
        answerThreeBtn.text = currentGameData.second[2].name
        answerFourBtn.text = currentGameData.second[3].name
    }

    private fun initViews() {
        questionTV = findViewById(R.id.quiz_activity_question_status)
        pointsCounterTV = findViewById(R.id.quiz_activity_score_counter)
        continueBtn = findViewById(R.id.quiz_activity_continue_button)
        answerOneBtn = findViewById(R.id.quiz_activity_answer_1_radio_button)
        answerTwoBtn = findViewById(R.id.quiz_activity_answer_2_radio_button)
        answerThreeBtn = findViewById(R.id.quiz_activity_answer_3_radio_button)
        answerFourBtn = findViewById(R.id.quiz_activity_answer_4_radio_button)
        answersRadioGroup = findViewById(R.id.quiz_activity_answers_radio_group)
    }

    //If all radio buttons are in unchecked state return true
    private fun answerIsSelected(): Boolean {
        val allFieldsUchecked = !answerOneBtn.isChecked && !answerTwoBtn.isChecked
                && !answerThreeBtn.isChecked && !answerFourBtn.isChecked
        return !allFieldsUchecked
    }

    //If answer is correct returns true and color a radio button in green colour
    //if not returns false and color in red
    private fun answerIsCorrect(): Boolean {
        val answers = currentGameData.second
        when {
            answerOneBtn.isChecked -> {
                if (answers[0].isCorrect) {
                    answerOneBtn.setColor(COLOR.GREEN)
                    return true
                } else answerOneBtn.setColor(COLOR.RED)
            }
            answerTwoBtn.isChecked -> {
                if (answers[1].isCorrect) {
                    answerTwoBtn.setColor(COLOR.GREEN)
                    return true
                } else answerTwoBtn.setColor(COLOR.RED)
            }
            answerThreeBtn.isChecked -> {
                if (answers[2].isCorrect) {
                    answerThreeBtn.setColor(COLOR.GREEN)
                    return true
                } else answerThreeBtn.setColor(COLOR.RED)

            }
            answerFourBtn.isChecked -> {
                if (answers[3].isCorrect) {
                    answerFourBtn.setColor(COLOR.GREEN)
                    return true
                } else answerFourBtn.setColor(COLOR.RED)
            }
            else -> {
                //Not selected any answer
                return false
            }
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(gameScoreSaved, currentGameScore)
        outState.putInt(questionNumberSaved, currentQuestionNumber)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentGameScore = savedInstanceState.getInt(gameScoreSaved)
        currentQuestionNumber = savedInstanceState.getInt(questionNumberSaved)
        currentGameData = Quiz(this).getQuestionData(currentQuestionNumber)
        updateViews()
    }

    companion object {
        private const val gameScoreSaved = "game_score"
        private const val questionNumberSaved = "question_number"
        private const val MAX_QUESTIONS_NUMBER = 3
        private const val NEXT_QUESTION_SHOW_DELAY = 1500L

        enum class COLOR {
            GREEN,
            RED,
            USUAL
        }
    }

}

//Extension function for radio button that sets a color for checked state
private fun RadioButton.setColor(color: QuizActivity.Companion.COLOR) {
    if (color == QuizActivity.Companion.COLOR.GREEN) {
        this.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
    }
    if (color == QuizActivity.Companion.COLOR.RED) {
        this.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
    }
    if (color == QuizActivity.Companion.COLOR.USUAL) {
        this.buttonTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.design_default_color_secondary
                )
            )
    }
}

//Extension function for radiobutton that return the default color for radiobutton
private fun RadioButton.reset() {
    this.setColor(QuizActivity.Companion.COLOR.USUAL)
    this.isChecked = false
}
