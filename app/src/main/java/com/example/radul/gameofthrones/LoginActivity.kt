package com.example.radul.gameofthrones

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.*
import android.util.Log
import android.content.ActivityNotFoundException
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.ContextCompat.startActivity




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)

    }

    fun tryLogin(ui: AnkoContext<MainActivity>, name: CharSequence?) {
        ui.doAsync {

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
                hint = "Fill your name"
            }


            button("Start Quiz") {
                onClick { ui.owner.tryLogin(ui, name.text) }

            }
            val emailDraft = editText {
                lines = 3
                hint = "Contact Developer"
            }

            button("Contact") {
                onClick {
                    try {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "plain/text"
                        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("some@email.address"))
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Complaining")
                        intent.putExtra(Intent.EXTRA_TEXT, emailDraft.text)
                        startActivity(getContext(), intent,intent.extras)

                    } catch (e: ActivityNotFoundException) {
                        //TODO smth
                    }

//            myRichView()
                }


            }
        }
    }
}



