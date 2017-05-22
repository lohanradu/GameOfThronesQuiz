package com.example.radul.gameofthrones

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)

    }

    fun tryLogin(ui: AnkoContext<MainActivity>, name: CharSequence?) {
        ui.doAsync {
            Thread.sleep(500)

            activityUiThreadWithContext {
                if (checkCredentials(name.toString())) {
                    toast("Greetings, " + name.toString() + ", Lord Commander!")
                    startActivity<QuizActivity>("counter" to 1, "score" to 0, "FLAG_ACTIVITY_CLEAR_TOP" to "True")
                } else {
                    toast("Please fill your name, Sir!")
                }
            }
        }
    }

    private fun checkCredentials(name: String) = name != ""
}



class MainActivityUi : AnkoComponent<MainActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            padding = dip(16)

            imageView(R.drawable.got).lparams {
               ; margin = dip(16)
                ;gravity = Gravity.CENTER

            }

            val name = editText {
                hintResource = R.string.name
                hint = "Username"
            }


            button("Start Quiz") {
                onClick {
                    ui.owner.tryLogin(ui, name.text)

                }
            }

            myRichView()
        }.applyRecursively(customStyle)

    }

}


