package com.visualizer.dsavisualization.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.os.Handler
import android.os.Looper

class BubbleSortView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }
    private var array: IntArray = intArrayOf()
    private var originalArray: IntArray = intArrayOf()
    private var currentIndices: Pair<Int, Int>? = null
    private val handler = Handler(Looper.getMainLooper())
    private var swapAnimationProgress = 0f
    private var swapping = false
    private val boxSize = 100f
    private val boxMargin = 20f // Margin between boxes
    var isSorted = false

    fun setArray(array: IntArray) {
        this.array = array
        this.originalArray = array.copyOf()
        isSorted = false
        requestLayout()
    }

    fun resetArray(originalArray: IntArray) {
        this.array = this.originalArray.copyOf()
        isSorted = false
        invalidate()
    }

    fun startBubbleSortAnimation() {
        isSorted = false
        bubbleSortStep(0, 0)
    }

    private fun bubbleSortStep(i: Int, j: Int) {
        if (i < array.size - 1) {
            if (j < array.size - 1 - i) {
                currentIndices = Pair(j, j + 1)
                if (array[j] > array[j + 1]) {
                    swapping = true
                    animateSwap(j, j + 1) {
                        val temp = array[j]
                        array[j] = array[j + 1]
                        array[j + 1] = temp
                        swapping = false
                        bubbleSortStep(i, j + 1)
                    }
                } else {
                    handler.postDelayed({ bubbleSortStep(i, j + 1) }, 1000) // Delay for animation
                }
                invalidate()
            } else {
                handler.postDelayed({ bubbleSortStep(i + 1, 0) }, 1000) // Delay for animation
            }
        } else {
            currentIndices = null
            isSorted = true
            invalidate()
        }
    }

    private fun animateSwap(index1: Int, index2: Int, onComplete: () -> Unit) {
        swapAnimationProgress = 0f
        val swapDuration = 1000L // Duration of swap animation

        handler.post(object : Runnable {
            override fun run() {
                swapAnimationProgress += 0.05f
                if (swapAnimationProgress < 1f) {
                    invalidate()
                    handler.postDelayed(this, swapDuration / 20)
                } else {
                    swapAnimationProgress = 1f
                    invalidate()
                    onComplete()
                }
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = ((array.size * (boxSize + boxMargin)) - boxMargin).toInt()
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2f
        val totalWidth = array.size * (boxSize + boxMargin) - boxMargin
        val startX = (width - totalWidth) / 2f

        for (i in array.indices) {
            paint.color = if (currentIndices != null && (i == currentIndices!!.first || i == currentIndices!!.second)) {
                Color.RED
            } else {
                Color.BLACK
            }

            val x = startX + i * (boxSize + boxMargin) + boxSize / 2

            // Draw the index above the box
            paint.style = Paint.Style.FILL
            canvas.drawText(i.toString(), x, centerY - boxSize, paint)

            if (swapping && currentIndices != null && (i == currentIndices!!.first || i == currentIndices!!.second)) {
                val (fromIndex, toIndex) = currentIndices!!
                val fromX = startX + fromIndex * (boxSize + boxMargin) + boxSize / 2
                val toX = startX + toIndex * (boxSize + boxMargin) + boxSize / 2

                if (i == fromIndex) {
                    val animatedX = fromX + swapAnimationProgress * (toX - fromX)
                    drawBox(canvas, animatedX, centerY, array[i].toString())
                } else if (i == toIndex) {
                    val animatedX = toX - swapAnimationProgress * (toX - fromX)
                    drawBox(canvas, animatedX, centerY, array[i].toString())
                }
            } else {
                drawBox(canvas, x, centerY, array[i].toString())
            }
        }

        if (isSorted) {
            for (i in array.indices) {
                paint.color = Color.BLACK
                val x = startX + i * (boxSize + boxMargin) + boxSize / 2
                drawBox(canvas, x, centerY, array[i].toString())
            }
        }
    }

    private fun drawBox(canvas: Canvas, x: Float, y: Float, text: String) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        canvas.drawRect(x - boxSize / 2, y - boxSize / 2 - 30, x + boxSize / 2, y + boxSize / 2 - 30, paint)
        paint.style = Paint.Style.FILL
        canvas.drawText(text, x, y - 30, paint)
    }
}
