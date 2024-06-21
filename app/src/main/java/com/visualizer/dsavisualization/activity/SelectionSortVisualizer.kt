package com.visualizer.dsavisualization.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.view.SelectionSortView

class SelectionSortVisualizer : AppCompatActivity() {

    private lateinit var selectionSortView: SelectionSortView
    private var originalArray: IntArray = intArrayOf()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_sort_visualizer)

        selectionSortView = findViewById(R.id.selectionSortView)
        val inputArrayEditText = findViewById<EditText>(R.id.inputArrayEditText)
        val startSortButton = findViewById<Button>(R.id.startSortButton)

        startSortButton.setOnClickListener {
            val inputText = inputArrayEditText.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(this, "Please enter values to sort", Toast.LENGTH_SHORT).show()
            } else {
                val values = parseInput(inputText)
                if (values != null) {
                    if (selectionSortView.isSorting()) {
                        resetAndRestartAnimation(values)
                    } else {
                        startSelectionSortAnimation(values)
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

    private fun startSelectionSortAnimation(values: IntArray) {
        originalArray = values.copyOf() // Store original array
        selectionSortView.setArray(values)
        selectionSortView.startSelectionSortAnimation()
    }

    private fun resetAndRestartAnimation(values: IntArray) {
        selectionSortView.resetArray(originalArray)
        handler.postDelayed({
            selectionSortView.setArray(values)
            selectionSortView.startSelectionSortAnimation()
        }, 1000)
    }
}