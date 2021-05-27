package com.gmail.konstantin.bezzemelnyi.quizapp.model

import android.content.Context
import com.gmail.konstantin.bezzemelnyi.quizapp.R

//Context in parameters to get string resources of application
class Quiz(private val context: Context) {
    private val quizMap: MutableMap<String, Array<Answer>> = setUpQuizMap()

    fun getQuestionData(questionNumber: Int): quizData {
        return Pair(
            getQuestionName(questionNumber),
            quizMap[getQuestionName(questionNumber)] ?: error("Answers are null")
        )
    }

    private fun setUpQuizMap(): MutableMap<String, Array<Answer>> {
        return mutableMapOf(
            //Question 1
            getQuestionName(1)
                    to arrayOf(
                Answer(getAnswerName(1, 1), true),
                Answer(getAnswerName(1, 2), false),
                Answer(getAnswerName(1, 3), false),
                Answer(getAnswerName(1, 4), false)
            ),
            //Question 2
            getQuestionName(2)
                    to arrayOf(
                Answer(getAnswerName(2, 1), false),
                Answer(getAnswerName(2, 2), true),
                Answer(getAnswerName(2, 3), false),
                Answer(getAnswerName(2, 4), false)
            ),
            //Question 3
            getQuestionName(3)
                    to arrayOf(
                Answer(getAnswerName(2, 1), false),
                Answer(getAnswerName(2, 2), false),
                Answer(getAnswerName(2, 3), false),
                Answer(getAnswerName(2, 4), true)
            ),
        )
    }

    //Get question name from string resources
    private fun getQuestionName(questionNumber: Int): String {
        return when (questionNumber) {
            1 -> context.getString(R.string.question_1)
            2 -> context.getString(R.string.question_2)
            3 -> context.getString(R.string.question_3)
            else -> error("Question with this question number does not exist.")
        }
    }

    //Get answer name by question and answer numbers
    private fun getAnswerName(questionNumber: Int, answerNumber: Int): String {
        when (questionNumber) {
            //Question 1
            1 -> {
                return when (answerNumber) {
                    1 -> context.getString(R.string.question_1_answer_1)
                    2 -> context.getString(R.string.question_1_answer_2)
                    3 -> context.getString(R.string.question_1_answer_3)
                    4 -> context.getString(R.string.question_1_answer_4)
                    else -> error("Answer with this number does not exist in question 1.")
                }
            }
            //Question 2
            2 -> {
                return when (answerNumber) {
                    1 -> context.getString(R.string.question_2_answer_1)
                    2 -> context.getString(R.string.question_2_answer_2)
                    3 -> context.getString(R.string.question_2_answer_3)
                    4 -> context.getString(R.string.question_2_answer_4)
                    else -> error("Answer with this number does not exist in question 2.")
                }
            }
            //Question 3
            3 -> {
                return when (answerNumber) {
                    1 -> context.getString(R.string.question_3_answer_1)
                    2 -> context.getString(R.string.question_3_answer_2)
                    3 -> context.getString(R.string.question_3_answer_3)
                    4 -> context.getString(R.string.question_3_answer_4)
                    else -> error("Answer with this number does not exist in question 3.")
                }
            }
            else -> return error("Question with this question number does not exist.")
        }
    }
}

typealias quizData = Pair<String, Array<Answer>>

