package com.visualizer.dsavisualization.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.view.InsertionSortView

class InsertionSortVisualizer : AppCompatActivity() {

    private lateinit var insertionSortView: InsertionSortView
    private var originalArray: IntArray = intArrayOf()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion_sort_visualizer)

        insertionSortView = findViewById(R.id.insertionSortView)
        val inputArrayEditText = findViewById<EditText>(R.id.inputArrayEditText)
        val startSortButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.startSortButton)

        startSortButton.setOnClickListener {
            val inputText = inputArrayEditText.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(this, "Please enter values to sort", Toast.LENGTH_SHORT).show()
            } else {
                val values = parseInput(inputText)
                if (values != null) {
                    startInsertionSortAnimation(values)
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

    private fun startInsertionSortAnimation(values: IntArray) {
        originalArray = values.copyOf() // Store original array
        insertionSortView.setArray(values)
        insertionSortView.startInsertionSortAnimation()
    }
}