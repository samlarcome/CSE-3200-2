package com.example.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"

// Custom View that is a subclass of View
class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs){
    private var currentBox: Box? = null
    private val boxes = mutableListOf<Box>()

    private val boxPaint = Paint().apply{
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply{
        color = 0xfff8efe0.toInt()
    }

    private var shapeCount = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current).also {
                    boxes.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                shapeCount += 1
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.d(TAG, "$action at x = ${current.x} , y = ${current.y}")

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)
        boxes.forEach {box ->
            //canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
            var squareSide = Math.min(box.height, box.width)
            if (box.width > box.height) {
                if (box.start.y > box.end.y && box.start.x > box.end.x) {
                    canvas.drawRect(
                        box.right - squareSide,
                        box.bottom - squareSide,
                        box.right,
                        box.bottom,
                        boxPaint
                    )
                } else if (box.start.x > box.end.x && box.start.y < box.end.y) {
                    canvas.drawRect(
                        box.right - squareSide,
                        box.top,
                        box.right,
                        box.top + squareSide,
                        boxPaint
                    )
                } else if (box.start.y > box.end.y && box.start.x < box.end.y) {
                    canvas.drawRect(
                        box.left,
                        box.bottom - squareSide,
                        box.left + squareSide,
                        box.bottom,
                        boxPaint
                    )
                } else {
                    canvas.drawRect(
                        box.left,
                        box.top,
                        box.left + squareSide,
                        box.top + squareSide,
                        boxPaint
                    )
                }
            } else {
                canvas.drawOval(
                    box.left,
                    box.top,
                    box.left + squareSide,
                    box.top + squareSide,
                    boxPaint
                )
            }
        }
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            if (shapeCount < 3) {
                invalidate()
            }
        }
    }
}
