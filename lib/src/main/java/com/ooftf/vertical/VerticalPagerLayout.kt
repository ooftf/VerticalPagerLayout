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
import kotlin.collections.ArrayList


/**
 * 高仿淘宝商品页上下分页布局
 * Created by master on 2016/3/28.
 */
class VerticalPagerLayout : FrameLayout {

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
    // private var positionMap = HashMap<Int, Any>()
    private var items = ArrayList<ItemInfo>()

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
                intercept = judgeIntercept(ev)
            }
            MotionEvent.ACTION_UP -> {
                intercept = true
            }
        }
        lastY = ev.y
        return intercept
    }

    private fun judgeIntercept(ev: MotionEvent): Boolean {
        val currentPager = viewForPosition(position)
        if (currentPager is EdgeWrapper) {
            if (currentPager.isTop() && ev.y - lastY > 0) {//顶部
                return true
            }
            if (currentPager.isBottom() && ev.y - lastY < 0) {//底部
                return true
            }

        } else {
            if (ev.y != lastY) {
                return true
            }
        }
        return false
    }

    private fun refreshViews() {
        //移除不必要View
        items.filter { it.position < position - 1 || it.position > position + 1 }
                .forEach { removeForItemInfo(it) }
        addNewView(position - 1)
        addNewView(position)
        addNewView(position + 1)
    }

    private fun removeForItemInfo(item: ItemInfo) {
        adapter?.destroyItem(this, item.position, item.obj)
        items.remove(item)
    }

    private fun addNewView(position: Int) {
        if (position < 0 && position >= adapter!!.count) return
        items.forEach {
            if (it.position == position) return
        }
        items.add(ItemInfo(position, adapter!!.instantiateItem(this, position)))

    }

    private fun viewForPosition(position: Int): View? {
        val itemInfo: ItemInfo? = itemInfoForPosition(position) ?: return null
        return itemInfo?.let { viewForItemInfo(it) }
    }

    private fun viewForItemInfo(item: ItemInfo): View? {
        (0 until childCount).forEach {
            if (adapter!!.isViewFromObject(getChildAt(it), item.obj)) {
                return getChildAt(it)
            }
        }
        return null
    }

    private fun itemInfoForPosition(position: Int): ItemInfo? {
        items.forEach { if (it.position == position) return it }
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
                gotoPager()
                lastY = event.y
                return false
            }
        }
        return true
    }

    /**
     * 计算应该定位到哪一页
     * 并开始滚动
     */
    private fun gotoPager() {
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
        invalidate()
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
        (0 until childCount).forEach {
            var view = getChildAt(it)
            var item = itemInfoForView(view)
            item?.let {
                view.layout(0, item.position * height, r - l, (item.position + 1) * height)
            }
        }
    }

    private fun itemInfoForView(view: View): ItemInfo? {
        items.forEach {
            if (adapter!!.isViewFromObject(view, it.obj)) return it
        }
        return null
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset() && !mScroller.isFinished) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        } else {
            refreshViews()
        }
    }

    class ItemInfo(var position: Int, var obj: Any)
}
