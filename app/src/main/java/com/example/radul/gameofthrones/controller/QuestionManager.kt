package com.example.radul.gameofthrones.controller

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import java.net.URL

/**
 * Created by radu on 17.10.2017.
 */

class QuestionManager private constructor() {

    private object Holder {
        val INSTANCE = QuestionManager()
    }

    companion object {
        val instance: QuestionManager by lazy { Holder.INSTANCE }
    }

    fun generateQuestion(characterNumber: Int): String? {
        return generateCharacterQuestion(characterNumber)
    }

    private fun generateCharacterQuestion(characterNumber: Int): String? {

        val result = URL("https://anapioficeandfire.com/api/characters/" + characterNumber.toString()).readText()
        val parser = Parser()
        val stringBuilder: StringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val question = "What is the title of " + json.string("name") + " ?"

        return question

    }

    private fun generateHouseQuestion(characterNumber: Int): String? {

        val result = URL("https://anapioficeandfire.com/api/houses/" + characterNumber.toString()).readText()
        val parser = Parser()
        val stringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val question = "What are the words of house " + json.string("name") + " ?"

        return question

    }
}
