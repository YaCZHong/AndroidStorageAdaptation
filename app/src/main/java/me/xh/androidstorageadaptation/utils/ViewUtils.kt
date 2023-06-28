package me.xh.androidstorageadaptation.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

/**
 * View to bitmap.
 *
 * @param view The view.
 * @return bitmap
 */
fun view2Bitmap(view: View): Bitmap {
    val drawingCacheEnabled = view.isDrawingCacheEnabled
    val willNotCacheDrawing = view.willNotCacheDrawing()
    view.isDrawingCacheEnabled = true
    view.setWillNotCacheDrawing(false)
    var drawingCache = view.drawingCache
    val bitmap: Bitmap
    if (null == drawingCache || drawingCache.isRecycled) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        drawingCache = view.drawingCache
        if (null == drawingCache || drawingCache.isRecycled) {
            bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
        } else {
            bitmap = Bitmap.createBitmap(drawingCache)
        }
    } else {
        bitmap = Bitmap.createBitmap(drawingCache)
    }
    view.setWillNotCacheDrawing(willNotCacheDrawing)
    view.isDrawingCacheEnabled = drawingCacheEnabled
    return bitmap
}