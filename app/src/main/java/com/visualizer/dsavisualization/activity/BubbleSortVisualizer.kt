package com.visualizer.dsavisualization.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.view.BubbleSortView

class BubbleSortVisualizer : AppCompatActivity() {

    private lateinit var bubbleSortView: BubbleSortView
    private var originalArray: IntArray = intArrayOf()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble_sort_visualizer)

        bubbleSortView = findViewById(R.id.bubbleSortView)
        val inputArrayEditText = findViewById<EditText>(R.id.inputArrayEditText)
        val startSortButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.startSortButton)

        startSortButton.setOnClickListener {
            val inputText = inputArrayEditText.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(this, "Please enter values to sort", Toast.LENGTH_SHORT).show()
            } else {
                val values = parseInput(inputText)
                if (values != null) {
                    if (bubbleSortView.isSorting()) {
                        resetAndRestartAnimation(values)
                    } else {
                        startBubbleSortAnimation(values)
                    }
                } else {
                    Toast.makeText(this, "Please enter values in correct format", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseInput(input: String): IntArray? {
        return try {
            input.split(Regex("[,\\s]+")).map { it.trim().toInt() }.toIntArray()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun startBubbleSortAnimation(values: IntArray) {
        originalArray = values.copyOf() // Store original array
        bubbleSortView.setArray(values)
        bubbleSortView.startBubbleSortAnimation()
    }

    private fun resetAndRestartAnimation(values: IntArray) {
        bubbleSortView.resetArray(originalArray)
        handler.postDelayed({
            bubbleSortView.setArray(values)
            bubbleSortView.startBubbleSortAnimation()
        }, 1000)
    }
}