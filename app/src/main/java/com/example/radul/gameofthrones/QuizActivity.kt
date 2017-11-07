package com.example.radul.gameofthrones

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.array
import com.example.radul.gameofthrones.controller.AnswerManager
import com.example.radul.gameofthrones.controller.QuestionManager
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.net.URL
import java.util.*

var correctAnswer: String? = ""

class QuizActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuizActivityUi().setContentView(this)

        doAsync {
            val pageCounter = intent.extras.getInt("counter")
            val characterNumber = 1 + Random().nextInt(1000)
            val question = generateQuestion(characterNumber)
            val answers = AnswerManager.instance.generatePoll(characterNumber)
            correctAnswer = answers?.get(0)
            Collections.shuffle(answers)
            uiThread {
                val textViewToChange = findViewById(R.id.question) as TextView
                textViewToChange.setText(question)

                val button1 = findViewById(R.id.button1) as Button
                button1.text = answers!![1]
                button1.visibility = View.VISIBLE
                val button2 = findViewById(R.id.button2) as Button
                button2.text = answers!![0]
                button2.visibility = View.VISIBLE
                val button3 = findViewById(R.id.button3) as Button
                button3.text = answers!![2]
                button3.visibility = View.VISIBLE
                val button4 = findViewById(R.id.button4) as Button
                button4.text = answers!![3]
                button4.visibility = View.VISIBLE

                val page = findViewById(R.id.page) as TextView
                page.text = pageCounter.toString()
            }
        }

    }

    private fun generateQuestion(characterNumber: Int): String? {

        return QuestionManager.instance.generateQuestion(characterNumber)
    }

    fun Answer(ui: AnkoContext<QuizActivity>, answer: CharSequence?, buttonId: Int) {
        ui.doAsync {
            activityUiThreadWithContext {
                val but = findViewById(buttonId)
                var score = intent.extras.getInt("score")
                if (checkAnswer(answer.toString())) {
                    toast("Correct")

                    but.setBackgroundColor(Color.GREEN)
                    score += 10

                } else {
                    toast("Wrong")

                    but.setBackgroundColor(Color.RED)
                }
                if (intent.extras.getInt("counter") > 5) {


                    toast("you scored " + score + "points")

                    startActivity<ScoreActivity>( "Score" to score)
                } else {
                    startActivity<QuizActivity>("counter" to (intent.extras.getInt("counter") + 1), "score" to score, "FLAG_ACTIVITY_CLEAR_TOP" to true)
                }
            }
        }
    }

    fun checkAnswer(answer: String?) = answer == correctAnswer


}


class QuizActivityUi : AnkoComponent<QuizActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }

    override fun createView(ui: AnkoContext<QuizActivity>) = with(ui) {
        verticalLayout {
            padding = dip(16)

            imageView(R.drawable.got).lparams {
                ; margin = dip(16)
                ;gravity = Gravity.CENTER

            }

            val question = textView("") {
                hintResource = R.string.name
                id = R.id.question
            }


            button() {
                id = R.id.button1
                visibility = View.GONE
                onClick {
                    ui.owner.Answer(ui, text, id)
                    //setBackgroundColor(Color.GREEN)

                }
            }
            button() {
                id = R.id.button2
                visibility = View.GONE
                onClick {
                    ui.owner.Answer(ui, text, id)

                }
            }
            button()
            {
                id = R.id.button3
                visibility = View.GONE
                onClick {
                    ui.owner.Answer(ui, text, id)

                }
            }
            button() {
                id = R.id.button4
                visibility = View.GONE
                onClick {

                    ui.owner.Answer(ui, text, id)
                }
            }
            val pageNumber = textView() {
                id = R.id.page
                gravity = CENTER
            }

            myRichView()
        }.applyRecursively(customStyle)

    }


}


