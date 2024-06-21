package com.visualizer.dsavisualization.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.os.Handler
import android.os.Looper

class SelectionSortView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }
    private var array: IntArray = intArrayOf()
    private var originalArray: IntArray = intArrayOf()
    private var currentIndices: Pair<Int, Int>? = null
    private var minIndex: Int? = null
    private var minElement: Int? = null
    private var currentPass = 0
    private val handler = Handler(Looper.getMainLooper())
    private var swapAnimationProgress = 0f
    private var swapping = false
    private val boxSize = 100f
    private val boxMargin = 20f // Margin between boxes

    fun setArray(array: IntArray) {
        this.array = array
        requestLayout()
    }

    fun resetArray(originalArray: IntArray) {
        this.array = originalArray.copyOf()
        this.originalArray = originalArray.copyOf()
        currentPass = 0
        invalidate()
    }

    fun startSelectionSortAnimation() {
        selectionSortStep(0)
    }

    fun isSorting(): Boolean {
        return swapping
    }

    private fun selectionSortStep(i: Int) {
        if (i < array.size - 1) {
            currentPass = i + 1
            minIndex = i
            minElement = array[minIndex!!]
            findMinElement(i, i + 1)
        } else {
            currentIndices = null
            minIndex = null
            currentPass = 0
            invalidate()
        }
    }

    private fun findMinElement(i: Int, j: Int) {
        if (j < array.size) {
            currentIndices = Pair(i, j)
            if (array[j] < array[minIndex!!]) {
                minIndex = j
                minElement = array[minIndex!!]
            }
            invalidate()
            handler.postDelayed({
                findMinElement(i, j + 1)
            }, 1000)
        } else {
            currentIndices = Pair(i, minIndex!!)
            swapElements(i, minIndex!!)
        }
    }

    private fun swapElements(index1: Int, index2: Int) {
        val temp = array[index1]
        array[index1] = array[index2]
        array[index2] = temp
        swapping = true
        animateSwap {
            swapping = false
            selectionSortStep(index1 + 1)
        }
    }

    private fun animateSwap(onComplete: () -> Unit) {
        swapAnimationProgress = 0f
        val swapDuration = 1500L // Duration of swap animation

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

        // Draw current pass number and current min element
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        val passText = "Pass: $currentPass"
        val minText = if (minIndex != null) "Current Min: $minElement" else "Current Min: N/A"
        canvas.drawText(passText, width / 2f, centerY - boxSize - 100, paint)
        canvas.drawText(minText, width / 2f, centerY - boxSize - 50, paint)

        for (i in array.indices) {
            paint.color = when {
                currentIndices != null && i == currentIndices!!.first -> Color.MAGENTA // Index to which the min element will be swapped
                currentIndices != null && i == currentIndices!!.second -> Color.RED   // Current index being compared
                i == minIndex -> Color.BLUE                                            // Current min element index
                else -> Color.BLACK
            }

            val x = startX + i * (boxSize + boxMargin) + boxSize / 2

            // Draw the index above the box
            paint.style = Paint.Style.FILL
            canvas.drawText(i.toString(), x, centerY - boxSize, paint)

            if (swapping && (i == currentIndices?.first || i == minIndex)) {
                val fromIndex = currentIndices?.first ?: i
                val toIndex = minIndex ?: i
                val fromX = startX + fromIndex * (boxSize + boxMargin) + boxSize / 2
                val toX = startX + toIndex * (boxSize + boxMargin) + boxSize / 2

                if (i == fromIndex) {
                    val animatedX = fromX + swapAnimationProgress * (toX - fromX)
                    drawBox(canvas, animatedX, centerY, array[i].toString(), Color.BLUE)
                } else if (i == toIndex) {
                    val animatedX = toX - swapAnimationProgress * (toX - fromX)
                    drawBox(canvas, animatedX, centerY, array[i].toString(), Color.MAGENTA)
                }
            } else {
                drawBox(canvas, x, centerY, array[i].toString(), paint.color)
            }
        }
    }

    private fun drawBox(canvas: Canvas, x: Float, y: Float, text: String, color: Int) {
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = 4f
        canvas.drawRect(x - boxSize / 2, y - boxSize / 2 - 30, x + boxSize / 2, y + boxSize / 2 - 30, paint)
        paint.style = Paint.Style.FILL
        canvas.drawText(text, x, y - 30, paint)
    }
}
