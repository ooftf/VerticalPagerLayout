package com.ooftf.vertical

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

/**
 * Created by 99474 on 2017/12/24 0024.
 */

class VerticalViewPager : ViewPager {
    private var transformer = VerticalTransformer()

    private var lastY: Float = 0f

    constructor(context: Context) : super(context) {
        setPageTransformer(false, transformer)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setPageTransformer(false, transformer)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.e("evY", "" + ev.y)
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> intercept = false
            MotionEvent.ACTION_MOVE -> {
                if (transformer.showing is EdgeWrapper) {
                    val edgeWrapper = transformer.showing as EdgeWrapper
                    if (edgeWrapper.isTop()&&ev.y - lastY > 0) {//顶部
                            intercept = true
                    }
                    if (edgeWrapper.isBottom()&&ev.y - lastY < 0) {//底部
                            intercept = true
                    }
                } else {
                    if (ev.y != lastY) {
                        intercept = true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                onTouchEvent(ev)
            }
        }
        lastY = ev.y
        super.onInterceptTouchEvent(swapTouchEvent(ev))
        swapTouchEvent(ev)
        Log.e("onInterceptTouchEvent", "" + intercept)
        return intercept
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> Log.e("onTouchEvent", "ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> Log.e("onTouchEvent", "ACTION_MOVE")
            MotionEvent.ACTION_UP -> Log.e("onTouchEvent", "ACTION_UP")
        }
        return super.onTouchEvent(swapTouchEvent(ev))
    }

    private fun swapTouchEvent(event: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val swappedX = event.y / height * width
        val swappedY = event.x / width * height
        event.setLocation(swappedX, swappedY)
        return event
    }
}
