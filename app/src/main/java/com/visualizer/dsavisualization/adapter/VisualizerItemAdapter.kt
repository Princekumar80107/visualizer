package com.visualizer.dsavisualization.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.activity.BubbleSortVisualizer
import com.visualizer.dsavisualization.activity.SelectionSortVisualizer
import com.visualizer.dsavisualization.activity.TreeVisualizer
import com.visualizer.dsavisualization.model.VisualizationItem
import com.visualizer.dsavisualization.view.BubbleSortView

class VisualizerItemAdapter(private var context: Context, private val visualizer: List<VisualizationItem>) :
    RecyclerView.Adapter<VisualizerItemAdapter.VisualizerViewHolder>() {

        inner class VisualizerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img: ImageView = itemView.findViewById(R.id.visualizerImg)
            var name: com.google.android.material.textview.MaterialTextView = itemView.findViewById(R.id.visualizerName)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisualizerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.visualizer_item, parent, false)
        return VisualizerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return visualizer.size
    }

    override fun onBindViewHolder(holder: VisualizerViewHolder, position: Int) {
        holder.name.text = visualizer[position].name
        holder.img.setImageResource(visualizer[position].img)

        holder.itemView.setOnClickListener {
            when (visualizer[position].index) {
                1 -> {
                    val intent = Intent(context, BubbleSortVisualizer::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context, SelectionSortVisualizer::class.java)
                    context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(context, TreeVisualizer::class.java)
                    context.startActivity(intent)
                }
            }
        }

    }
}