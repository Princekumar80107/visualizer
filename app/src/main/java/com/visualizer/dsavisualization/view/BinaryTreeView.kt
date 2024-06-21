package com.visualizer.dsavisualization.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.view.View
import android.os.Handler
import com.visualizer.dsavisualization.model.TreeNode

class BinaryTreeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var treeRoot: TreeNode<String>? = null
    private val paint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val highlightPaint: Paint = Paint()
    private val handler = Handler()

    private var animationNodes: List<TreeNode<String>> = emptyList()
    private var currentAnimationIndex = 0

    private var scaleFactor = 1.0f
    private val scaleGestureDetector: ScaleGestureDetector

    private var animationComplete = false

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 5f

        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER

        highlightPaint.color = Color.RED
        highlightPaint.strokeWidth = 5f

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    fun setTree(root: TreeNode<String>?) {
        treeRoot = root
        requestLayout()
    }

    fun startTraversalAnimation(nodes: List<TreeNode<String>>, onComplete: () -> Unit) {
        animationNodes = nodes
        currentAnimationIndex = 0
        animationComplete = false
        handler.post(object : Runnable {
            override fun run() {
                if (currentAnimationIndex < animationNodes.size) {
                    invalidate()
                    currentAnimationIndex++
                    handler.postDelayed(this, 1000) // Increased delay for slower animation
                } else {
                    animationComplete = true
                    invalidate()
                    onComplete()
                }
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = 2000
        val height = 2000
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor)
        treeRoot?.let {
            drawNode(canvas, it, width / 2f, 100f, width / 4f)
        }
        canvas.restore()
    }

    private fun drawNode(canvas: Canvas, node: TreeNode<String>, x: Float, y: Float, offset: Float) {
        val paintToUse = if (animationComplete) paint else if (animationNodes.getOrNull(currentAnimationIndex - 1) == node) highlightPaint else paint
        canvas.drawCircle(x, y, 30f, paintToUse)
        canvas.drawText(node.value, x, y + 15f, textPaint)

        val childYOffset = 200f

        node.left?.let {
            val childX = x - offset
            val childY = y + childYOffset
            canvas.drawLine(x, y + 30f, childX, childY - 30f, paint)
            drawNode(canvas, it, childX, childY, offset / 2)
        }

        node.right?.let {
            val childX = x + offset
            val childY = y + childYOffset
            canvas.drawLine(x, y + 30f, childX, childY - 30f, paint)
            drawNode(canvas, it, childX, childY, offset / 2)
        }
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 5.0f)
            invalidate()
            return true
        }
    }
}