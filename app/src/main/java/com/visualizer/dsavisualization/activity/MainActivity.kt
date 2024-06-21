package com.visualizer.dsavisualization.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.adapter.VisualizerItemAdapter
import com.visualizer.dsavisualization.model.VisualizationItem

class MainActivity : AppCompatActivity() {

    private var visualizerList = mutableListOf<VisualizationItem>()
    private lateinit var adapter: VisualizerItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
    }

    private fun setUpViews() {
        setUpData()
        setUpRecyclerView()
    }

    private fun setUpData() {
        visualizerList.add(VisualizationItem(R.drawable.bubble, "Bubble Sort Visualizer",  1))
        visualizerList.add(VisualizationItem(R.drawable.selection, "Selection Sort Visualizer",  2))
        visualizerList.add(VisualizationItem(R.drawable.tree, "Tree Visualizer",  3))
    }

    private fun setUpRecyclerView() {
        val visualizerRV = findViewById<RecyclerView>(R.id.visualizerRV)
        adapter = VisualizerItemAdapter(this, visualizerList)
        visualizerRV.layoutManager = GridLayoutManager(this, 2)
        visualizerRV.adapter = adapter
    }
}