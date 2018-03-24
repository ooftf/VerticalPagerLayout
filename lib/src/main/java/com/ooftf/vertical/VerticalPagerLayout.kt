package com.ooftf.vertical

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.PagerAdapter
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller


/**
 * 高仿淘宝商品页上下分页布局
 * Created by master on 2016/3/28.
 */
class VerticalPagerLayout : FrameLayout {
    /**
     * 松开时布局滑动动画时间
     */
    private var SCROLL_DURATION = 700
    /**
     * 触发拦截的距离
     */
    val TRIGGER_INTERCEPT_VALUE = 17;
    /**
     * 触发翻页的速度
     */
    val TRIGGER_PAGE_VELOCITY = 3000;

    var offscreenPageLimit = 1;
    private var mScroller: Scroller
    var velocityY = 0f
    val gestureDetector: GestureDetector
    private var items = ArrayList<ItemInfo>()
    var adapter: PagerAdapter? = null
        set(value) {
            field = value
            resetLayout()
        }

    /**
     * 当更换adapter之后，重置所有信息
     */
    private fun resetLayout() {
        removeAllItem()
        scrollTo(0, 0)
        refreshViews()
    }

    //private var positionMap = HashMap<Int, Any>()


    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        gestureDetector = GestureDetector(GestureListener())
        mScroller = Scroller(context)
    }

    // float startY;
    private var lastY = 0.toFloat()
    var actionDownPage = 0;
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDownPage = getCurrentPage()
                intercept = false
            }
            MotionEvent.ACTION_MOVE -> {
                intercept = judgeIntercept(ev)
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
            }
        }
        lastY = ev.y
        return intercept
    }

    override fun scrollTo(x: Int, y: Int) {
        refreshViews()
        super.scrollTo(x, y)
    }


    private fun judgeIntercept(ev: MotionEvent): Boolean {
        val currentView = viewForPosition(getCurrentPage())
        if (currentView is EdgeWrapper) {
            if (currentView.isTop() && ev.y - lastY > TRIGGER_INTERCEPT_VALUE) {//顶部
                return true
            }
            if (currentView.isBottom() && ev.y - lastY < -TRIGGER_INTERCEPT_VALUE) {//底部
                return true
            }
        } else if (Math.abs(ev.y - lastY) > TRIGGER_INTERCEPT_VALUE) {//当child没有滚动布局的时候，只要触摸再Y轴有移动就拦截
            return true
        }
        return false
    }

    private fun refreshViews() {
        //移除不必要View
        adapter ?: return
        adapter?.startUpdate(this)
        items
                .filter { it.position < getCurrentPage() - offscreenPageLimit || it.position > getCurrentPage() + offscreenPageLimit }
                .forEach {
                    removeForItemInfo(it)
                }
        (getCurrentPage() - offscreenPageLimit..getCurrentPage() + offscreenPageLimit).forEach {
            addNewView(it)
        }
        adapter?.setPrimaryItem(this,getCurrentPage(),itemInfoForPosition(getCurrentPage())!!.obj)
        adapter?.finishUpdate(this)

    }
    private fun removeAllItem(){
        adapter?.startUpdate(this)
        items.forEach {
            adapter?.destroyItem(this, it.position, it.obj)
        }
        items.clear()
        adapter?.finishUpdate(this)
    }
    private fun removeForItemInfo(item: ItemInfo) {
        adapter?.destroyItem(this, item.position, item.obj)
        items.remove(item)
    }

    private fun addNewView(position: Int) {
        if (position < 0 && position >= adapter!!.count) return
        items.forEach { if (it.position == position) return }
        items.add(ItemInfo(position, adapter!!.instantiateItem(this, position)))
    }

    private fun viewForPosition(position: Int): View? {
        val item = itemInfoForPosition(position)
        return viewFroItemInfo(item!!)
    }

    private fun viewFroItemInfo(itemInfo: ItemInfo): View? {
        (0 until childCount).forEach {
            if (adapter!!.isViewFromObject(getChildAt(it), itemInfo.obj)) {
                return getChildAt(it)
            }
        }
        return null
    }

    private fun itemInfoForPosition(position: Int): ItemInfo? {
        items.forEach {
            if (position == it.position) {
                return it
            }
        }
        return null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScroller.forceFinished(true)
        val fling = gestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val expect = scrollY + (lastY - event.y)
                if (expect >= 0 && expect <= height * (adapter!!.count - 1)) {
                    scrollBy(0, (lastY - event.y).toInt())
                }
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                judgePage(fling)
                lastY = event.y
                return false
            }
            MotionEvent.ACTION_CANCEL -> {
                judgePage(fling)
                lastY = event.y
                return false
            }
        }
        return true
    }

    /**
     * 判断应该停留在哪一页
     */
    private fun judgePage(fling: Boolean) {
        if (fling) {
            if (velocityY < 0) {//下一页
                setCurrentItem(actionDownPage + 1)//此处不用getCurrentPage而是用actionDownPage 是为了防止在当手势滑动超过半个屏幕，并且触发fling的时候会连续翻两页
            } else {//上一页
                setCurrentItem(actionDownPage - 1)
            }
            return
        }
        setCurrentItem(getCurrentPage())
    }

    /**
     * 滚动到指定页面
     */
    fun setCurrentItem(page: Int, smooth: Boolean = true) {
        if (height == 0) {
            post { setCurrentItem(page, smooth) }
            return
        }
        if (smooth) {
            mScroller.startScroll(0, scrollY, 0, height * pageController(page) - scrollY, SCROLL_DURATION)
            invalidate()
        } else {
            scrollTo(0, height * pageController(page))
        }
    }

    /**
     * 防止position超出边缘
     */
    private fun pageController(src: Int): Int {
        if (src < 0) {
            return 0
        }
        return if (src > adapter!!.count - 1) {
            adapter!!.count - 1
        } else src
    }

    /**
     * 得到的是当前占有试图最大的页面
     */
    private fun getCurrentPage(): Int {
        if (height == 0) return 0
        return Math.round(scrollY.toFloat() / height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val height = b - t
        (0 until childCount).forEach {
            var itemInfo = itemInfoForView(getChildAt(it));
            getChildAt(it).layout(0, itemInfo!!.position * height, r - l, (itemInfo!!.position + 1) * height)
        }
    }

    private fun itemInfoForView(child: View): ItemInfo? {
        items.forEach {
            if (adapter!!.isViewFromObject(child, it.obj)) {
                return it
            }
        }
        return null
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset() && !mScroller.isFinished) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    /* fun getCurrentPage(): Int {
         if (height == 0) return 0
         return Math.round(scrollY.toFloat() / height)
     }*/

    class ItemInfo(var position: Int, var obj: Any)
    /**
     * 仅用作与判断Fling
     */
    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            this@VerticalPagerLayout.velocityY = velocityY
            Log.e("velocityY", velocityY.toString())
            return Math.abs(velocityY) > TRIGGER_PAGE_VELOCITY
        }
    }


}