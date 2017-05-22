package com.example.radul.gameofthrones

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.CENTER
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.array
import com.beust.klaxon.string
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONArray
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

var correctAnswer:String? = ""

class QuizActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuizActivityUi().setContentView(this)

        doAsync {
            val pageCounter = intent.extras.getInt("counter")
            val characterNumber = 1 + Random().nextInt(1000)
            val question = generateQuestion(characterNumber)
            val answer = generateAnswer(characterNumber)
            correctAnswer = answer
            val answer2 = generateAnswer((1 + Random().nextInt(1000)))
            val answer3 = generateAnswer((1 + Random().nextInt(1000)))
            val answer4 = generateAnswer((1 + Random().nextInt(1000)))
            //Thread.sleep(2000)
            uiThread {
                val textViewToChange = findViewById(R.id.question) as TextView
                textViewToChange.setText(question)

                val button1 = findViewById(R.id.button1) as Button
                button1.text = answer2
                val button2 = findViewById(R.id.button2) as Button
                button2.text = answer
                val button3 = findViewById(R.id.button3) as Button
                button3.text= answer3
                val button4 = findViewById(R.id.button4) as Button
                button4.text= answer4

                val page = findViewById(R.id.page) as TextView
                page.text = pageCounter.toString()
            }
        }

    }

    private fun  generateQuestion(characterNumber: Int): String? {

        return generateCharacterQuestion(characterNumber)
    }

    private fun generateCharacterQuestion(characterNumber: Int) : String? {

        val result = URL("https://anapioficeandfire.com/api/characters/" + characterNumber.toString()).readText()
        val parser: Parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val question = "What is the title of " + json.string("name") + " ?"

        return question

    }

    private fun generateHouseQuestion(characterNumber: Int) : String? {

        val result = URL("https://anapioficeandfire.com/api/houses/" + characterNumber.toString()).readText()
        val parser: Parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val question = "What are the words of house " + json.string("name") + " ?"

        return question

    }
    private fun  generateAnswer(characterNumber: Int): String? {

        val result = URL("https://anapioficeandfire.com/api/characters/"+characterNumber.toString()).readText()
        val parser: Parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        var answer : String? = ""
       try {
          answer  = json.array<String>("titles")?.get(0)
       }catch (e : Exception){
           answer = "None"
       }
           return answer

    }
    fun Answer(ui: AnkoContext<QuizActivity>, answer: CharSequence?, buttonId:Int) {
        ui.doAsync {
            Thread.sleep(500)

            activityUiThreadWithContext {
                val but = findViewById(buttonId)
                var score = intent.extras.getInt("score")
                 if(checkAnswer(answer.toString())){
                     toast("Correct")

                     but.setBackgroundColor(Color.GREEN)
                     score +=10

                 }else{
                     toast("Wrong")

                     but.setBackgroundColor(Color.RED)
                 }
                if(intent.extras.getInt("counter") > 5){


                    toast("you scored " + score + "points" )
                    Thread.sleep(500)
                    val homeIntent = Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish()
                }else {
                    startActivity<QuizActivity>("counter" to (intent.extras.getInt("counter") + 1), "score" to score, "FLAG_ACTIVITY_CLEAR_TOP" to true)
                }
                }
        }
    }

     fun  checkAnswer(answer: String?) = answer == correctAnswer


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
                onClick {
                    ui.owner.Answer(ui, text, id)
                    //setBackgroundColor(Color.GREEN)

                }
            }
            button() {
                id = R.id.button2
                onClick {
                    ui.owner.Answer(ui, text, id)

                }
            }
            button() {
                id = R.id.button3
                onClick {
                    ui.owner.Answer(ui, text, id)

                }
            }
            button() {
                id = R.id.button4
                onClick {

                    ui.owner.Answer(ui, text, id)
                }
            }
            val pageNumber = textView(){
                id = R.id.page
                gravity = CENTER
            }

            myRichView()
        }.applyRecursively(customStyle)

    }




}


