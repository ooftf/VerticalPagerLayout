package com.ooftf.vertical

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.PagerAdapter
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.collections.HashMap


/**
 * 高仿淘宝商品页上下分页布局
 * Created by master on 2016/3/28.
 */
class VerticalPagerLayout : FrameLayout {
    internal var TAG = "FlipLayout"


    /**
     * 松开时布局滑动动画时间
     */
    private var duration = 400
    private lateinit var mScroller: Scroller
    private var position = 0
    var adapter: PagerAdapter? = null
        set(value) {
            field = value
            refreshViews()
        }
    private var positionMap = HashMap<Int, Any>()

    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        mScroller = Scroller(context)
    }

    // float startY;
    private var lastY: Float = 0.toFloat()

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
            }
            MotionEvent.ACTION_MOVE -> {
                val currentPager = findViewByPosition(position)
                if (currentPager is EdgeWrapper) {
                    if (currentPager.isTop() && ev.y - lastY > 0) {//顶部
                        intercept = true
                    }
                    if (currentPager.isBottom() && ev.y - lastY < 0) {//底部
                        intercept = true
                    }
                } else {
                    if (ev.y != lastY) {
                        intercept = true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                intercept = true
            }
        }
        lastY = ev.y
        return intercept
    }

    private fun refreshViews() {
        //移除不必要View
        positionMap.keys
                .filter { it < position - 1 || it > position + 1 }
                .forEach {
                    removeByPosition(it)
                }
        addView(position - 1)
        addView(position)
        addView(position + 1)
    }

    private fun removeByPosition(position: Int) {
        adapter?.destroyItem(this, position, positionMap[position])
        positionMap.remove(position)
    }

    private fun addView(position: Int) {
        adapter?.let { adapter ->
            if (position < 0 && position >= adapter.count) return
            if (positionMap.containsKey(position)) return
            positionMap.put(position, adapter.instantiateItem(this, position))
        }
    }

    private fun findViewByPosition(position: Int): View? {
        val obj = positionMap[position]
        (0 until childCount).forEach {
            if (adapter!!.isViewFromObject(getChildAt(it), obj)) {
                return getChildAt(it)
            }
        }
        return null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("ACTION_MOVE", "lastY:$lastY -- event.y:${event.y}")
                scrollBy(0, (lastY - event.y).toInt())
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.e("ACTION_MOVE", "lastY:$lastY -- event.y:${event.y}")
                val protectionRange = height / 5
                if (scrollY < height * position - protectionRange) {//上一页
                    position = pageController(position - 1)
                    mScroller.startScroll(0, scrollY, 0, height * position - scrollY, duration)
                } else if (scrollY >= height * position - protectionRange && scrollY <= height * position + protectionRange) {
                    //留在本页
                    mScroller.startScroll(0, scrollY, 0, position * height - scrollY, duration)
                    //position = position;
                } else if (scrollY > height * position + protectionRange) {
                    //下一页
                    position = pageController(position + 1)
                    mScroller.startScroll(0, scrollY, 0, height * position - scrollY, duration)
                }
                lastY = event.y
                invalidate()
                return false
            }
        }
        return true
    }

    private fun pageController(src: Int): Int {
        if (src < 0) {
            return 0
        }
        return if (src > adapter!!.count - 1) {
            adapter!!.count - 1
        } else src
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val height = b - t
        positionMap.keys.forEach {
            findViewByPosition(it)?.layout(0, it * height, r - l, (it + 1) * height)
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }else{
            refreshViews()
        }
    }
}
