package com.example.radul.gameofthrones

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import org.jetbrains.anko.*


class ScoreActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScoreActivityUi().setContentView(this)
        val score = intent.extras.getInt("Score")
        val message = findViewById(R.id.bye) as TextView
        message.text = "Congratulations, you scored " + score
    }

}

class ScoreActivityUi : AnkoComponent<ScoreActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is TextView -> v.textSize = 24f
        }
    }

    override fun createView(ui: AnkoContext<ScoreActivity>) = with(ui) {
        verticalLayout {
            padding = dip(16)

            imageView(R.drawable.got).lparams {
                 margin = dip(16)
                gravity = Gravity.CENTER

            }

            val bye = textView("Congratulations you scored ") {
                hintResource = R.string.bye
                id = R.id.bye
            }

            myRichView()
        }.applyRecursively(customStyle)
    }
}


