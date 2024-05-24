package com.example.checksolvekokin

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.min
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var totalProblems: TextView
    private lateinit var correctCount: TextView
    private lateinit var incorrectCount: TextView
    private lateinit var percentage: TextView
    private lateinit var minTimeView: TextView
    private lateinit var maxTimeView: TextView
    private lateinit var avgTimeView: TextView
    private lateinit var operand1: TextView
    private lateinit var operator: TextView
    private lateinit var operand2: TextView
    private lateinit var result: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var startButton: Button

    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var totalAnswers = 0
    private var startTime: Long = 0
    private var currentAnswer = 0.0
    private var generatedAnswer = 0.0

    private var minTime = Long.MAX_VALUE
    private var maxTime = Long.MIN_VALUE
    private var totalTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalProblems = findViewById(R.id.total_problems)
        correctCount = findViewById(R.id.correct)
        incorrectCount = findViewById(R.id.incorrect)
        percentage = findViewById(R.id.percentage)
        minTimeView = findViewById(R.id.min_time)
        maxTimeView = findViewById(R.id.max_time)
        avgTimeView = findViewById(R.id.avg_time)
        operand1 = findViewById(R.id.operand1)
        operator = findViewById(R.id.operator)
        operand2 = findViewById(R.id.operand2)
        result = findViewById(R.id.result)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        startButton = findViewById(R.id.start_button)

        startButton.setOnClickListener {
            generateProblem()
            startButton.isEnabled = false
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            startTime = SystemClock.elapsedRealtime()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }
    }

    private fun generateProblem() {
        val num1 = Random.nextInt(10, 100)
        val num2 = Random.nextInt(10, 100)
        val operators = listOf("+", "-", "*", "/")
        val selectedOperator = operators.random()

        operand1.text = num1.toString()
        operand2.text = num2.toString()
        operator.text = selectedOperator

        currentAnswer = when (selectedOperator) {
            "+" -> num1 + num2.toDouble()
            "-" -> num1 - num2.toDouble()
            "*" -> num1 * num2.toDouble()
            "/" -> {
                val divisor = if (num2 != 0) num2 else Random.nextInt(1, 10)
                operand2.text = divisor.toString()
                (num1 / divisor.toDouble()).toString().toDouble()
            }
            else -> 0.0
        }

        generatedAnswer = if (Random.nextBoolean()) currentAnswer else Random.nextDouble(0.0, 200.0)
        result.text = String.format("%.2f", generatedAnswer)
    }

    private fun checkAnswer(userSaidTrue: Boolean) {
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val correct = if (userSaidTrue) generatedAnswer == currentAnswer else generatedAnswer != currentAnswer

        if (correct) {
            correctAnswers++
        } else {
            incorrectAnswers++
        }
        totalAnswers++

        updateStatistics(elapsedTime)

        val resultMessage = if (correct) "ПРАВИЛЬНО" else "НЕ ПРАВИЛЬНО"
        result.text = "$resultMessage\nВремя: ${elapsedTime}мс"

        startButton.isEnabled = true
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }

    private fun updateStatistics(elapsedTime: Long) {
        totalTime += elapsedTime
        if (elapsedTime < minTime) minTime = elapsedTime
        if (elapsedTime > maxTime) maxTime = elapsedTime

        totalProblems.text = "Итого проверено примеров: $totalAnswers"
        correctCount.text = "Правильно: $correctAnswers"
        incorrectCount.text = "Не правильно: $incorrectAnswers"
        val percent = if (totalAnswers > 0) (correctAnswers.toDouble() / totalAnswers * 100) else 0.0
        percentage.text = String.format("%.2f%%", percent)

        val avgTime = if (totalAnswers > 0) totalTime.toDouble() / totalAnswers else 0.0
        minTimeView.text = "Минимум: ${minTime / 1000.0}"
        maxTimeView.text = "Максимум: ${maxTime / 1000.0}"
        avgTimeView.text = "Среднее: ${String.format("%.2f", avgTime / 1000.0)}"
    }
}
