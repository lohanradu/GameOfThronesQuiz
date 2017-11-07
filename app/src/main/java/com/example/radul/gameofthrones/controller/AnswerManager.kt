package com.example.radul.gameofthrones.controller

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.array
import com.example.radul.gameofthrones.correctAnswer
import org.jetbrains.anko.doAsync
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Created by radu on 17.10.2017.
 */

class AnswerManager private constructor() {

    private object Holder {
        val INSTANCE = AnswerManager()
    }

    companion object {
        val instance: AnswerManager by lazy { Holder.INSTANCE }
    }

    public fun generateAnswer(characterNumber: Int): String? {

        val result = URL("https://anapioficeandfire.com/api/characters/" + characterNumber.toString()).readText()
        val parser = Parser()
        val stringBuilder = StringBuilder(result)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        var answer: String? = ""
        try {
            answer = json.array<String>("titles")?.get(0)
        } catch (e: Exception) {
            answer = "None"
        }
        when (answer) {
            "" -> return "None"
            else -> {
                return answer
            }
        }

    }

    fun generatePoll(characterNumber: Int): List<String?>? {
        val answers = ArrayList<String?>()
        val answer = generateAnswer(characterNumber)
        answers.add(answer)
        while (answers.size < 4) {
            val wrongAnswer = generateAnswer((1 + Random().nextInt(1000)))
            if (!answers.contains(wrongAnswer) && !wrongAnswer.equals("None")) {
                answers.add(wrongAnswer)
            }
        }

        return answers

    }

}

