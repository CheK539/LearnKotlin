package com.example.learnkotlin.viewsChanger

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.drawToBitmap

class ColorButtons(private val colorsLayout: LinearLayout, private val imageView: ImageView) {
    init {
        colorButtons()
    }

    private fun colorButtons() {
        val childCount = colorsLayout.childCount
        val bitmap = colorsLayout.drawToBitmap()

        for (i in 0 until childCount) {
            val view = colorsLayout.getChildAt(i)
            view.setOnClickListener { changeColor(it) }
            val (x, y) = getCenterPosition(view)
            val pixel = bitmap.getPixel(x, y)
            view.setBackgroundColor(pixel)
        }
    }

    private fun changeColor(view: View) =
        imageView.setBackgroundColor((view.background as ColorDrawable).color)

    private fun getCenterPosition(view: View): Pair<Int, Int> {
        val rectanglePosition = IntArray(2)
        val layoutPosition = IntArray(2)
        view.getLocationOnScreen(rectanglePosition)
        colorsLayout.getLocationOnScreen(layoutPosition)
        val x = rectanglePosition[0] - layoutPosition[0] + view.width / 2
        val y = rectanglePosition[1] - layoutPosition[1] + view.height / 2
        return Pair(x, y)
    }
}