package com.example.day7threadsorting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarsView(context: Context, attr: AttributeSet) : View(context, attr){
    enum class DATA_COLOURS{
        DATA_COLOR_BLUE,
        DATA_COLOR_RED,
        DATA_COLOR_GREEN
    }
    companion object {
        var MAX_VALUE = 100
        val COLORS_COUNT = DATA_COLOURS.values().size
    }

    private val paintBlueLine = Paint().also {
        it.style = Paint.Style.STROKE
        it.color = Color.BLUE
    }
    private val paintRedLine = Paint().also {
        it.style = Paint.Style.STROKE
        it.color = Color.RED
    }
    private val paintGreeenLine = Paint().also {
        it.style = Paint.Style.STROKE
        it.color = Color.GREEN
    }

    private var dataBlue : Array<Int>? = null
    private var dataRed : Array<Int>? = null
    private var dataGreen : Array<Int>? = null

    fun setData(data_color:Int, dataForSorting: Array<Int>){
        when(data_color){
            DATA_COLOURS.DATA_COLOR_BLUE.ordinal -> dataBlue = dataForSorting
            DATA_COLOURS.DATA_COLOR_RED.ordinal -> dataRed = dataForSorting
            DATA_COLOURS.DATA_COLOR_GREEN.ordinal -> dataGreen = dataForSorting
            else -> throw Exception("In BarsView.setData(data_color, dataForSorting). Parameter data_color is wrong!")
        }
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas != null && (dataBlue != null || dataRed != null || dataGreen != null) ){
            for (y in 0 until height){
                when(y % COLORS_COUNT) {
                    DATA_COLOURS.DATA_COLOR_BLUE.ordinal ->
                        if (dataBlue != null) {
                            val lineWidth = width * (dataBlue!![y/COLORS_COUNT].toFloat() / MAX_VALUE)
                            canvas.drawLine(0F, y.toFloat(), lineWidth, y.toFloat(), paintBlueLine)
                        }
                    DATA_COLOURS.DATA_COLOR_RED.ordinal ->
                        if (dataRed != null) {
                            val lineWidth = width * (dataRed!![y/COLORS_COUNT].toFloat() / MAX_VALUE)
//                            println("draw DATA_COLOR_RED lineWidth = $lineWidth")
                            canvas.drawLine(0F, y.toFloat(), lineWidth, y.toFloat(), paintRedLine)
                        }
                    DATA_COLOURS.DATA_COLOR_GREEN.ordinal ->
                        if (dataGreen != null) {
                            val lineWidth = width * (dataGreen!![y/COLORS_COUNT].toFloat() / MAX_VALUE)
                            canvas.drawLine(0F, y.toFloat(), lineWidth, y.toFloat(), paintGreeenLine)
                        }
                    else -> throw Exception("BarsView.COLORS_COUNT is wrong!")
                }
            }
        }
    }
}