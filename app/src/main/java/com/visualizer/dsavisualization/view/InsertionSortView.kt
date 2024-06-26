package com.visualizer.dsavisualization.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.os.Handler
import android.os.Looper

class InsertionSortView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }
    private var array: IntArray = intArrayOf()
    private var currentIndex: Int = -1
    private var comparedIndex: Int = -1
    private var sortedUntilIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private var swapAnimationProgress = 0f
    private var swapping = false
    private val boxSize = 100f
    private val boxMargin = 20f

    fun setArray(array: IntArray) {
        this.array = array
        sortedUntilIndex = 0
        invalidate()
    }

    fun startInsertionSortAnimation() {
        insertionSortStep(1)
    }

    private fun insertionSortStep(index: Int) {
        if (index < array.size) {
            val key = array[index]
            var j = index - 1

            handler.post(object : Runnable {
                override fun run() {
                    if (j >= 0 && array[j] > key) {
                        comparedIndex = j
                        swapping = true
                        animateSwap(j, j + 1) {
                            val ele = array[j + 1]
                            array[j + 1] = array[j]
                            array[j] = ele
                            j--
                            invalidate()
                            handler.postDelayed(this, 1000) // Delay for visualization
                        }
                    } else {
                        comparedIndex = -1
                        currentIndex = j + 1
                        array[j + 1] = key
                        sortedUntilIndex = index
                        invalidate()
                        handler.postDelayed({
                            currentIndex = -1
                            insertionSortStep(index + 1)
                        }, 1000) // Delay for visualization
                    }
                }
            })
        } else {
            sortedUntilIndex = array.size - 1
            comparedIndex = -1
            currentIndex = -1
            invalidate()
        }
    }

    private fun animateSwap(index1: Int, index2: Int, onComplete: () -> Unit) {
        swapAnimationProgress = 0f
        val swapDuration = 1000L // Duration for swap animation

        handler.post(object : Runnable {
            override fun run() {
                swapAnimationProgress += 0.05f
                if (swapAnimationProgress < 1f) {
                    invalidate()
                    handler.postDelayed(this, swapDuration / 20)
                } else {
                    swapAnimationProgress = 1f
                    invalidate()
                    swapping = false
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
            paint.color = when {
                i == currentIndex -> Color.BLUE
                i == comparedIndex -> Color.RED
                i <= sortedUntilIndex -> Color.MAGENTA
                else -> Color.BLACK
            }

            val x = startX + i * (boxSize + boxMargin) + boxSize / 2

            if (swapping && (i == currentIndex || i == comparedIndex)) {
                val fromX = startX + comparedIndex * (boxSize + boxMargin) + boxSize / 2
                val toX = startX + (comparedIndex + 1) * (boxSize + boxMargin) + boxSize / 2
                val animatedX = fromX + swapAnimationProgress * (toX - fromX)

                drawBox(canvas, animatedX, centerY, array[i].toString())
            } else {
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
