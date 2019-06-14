package com.ooftf.vertical.nested

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.PagerAdapter
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller


/**
 * 高仿淘宝商品页上下分页布局
 * Created by master on 2016/3/28.
 */
class VerticalPagerLayout : FrameLayout, NestedScrollingParent2 {
    var helper = NestedScrollingParentHelper(this)
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray?, type: Int) {
        moved = true
        Log.e("onNestedPreScroll", "dy::" + dy)
        if (scrollY % height != 0) {
            scrollBy(0, dy)
            consumed?.set(1, dy)
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        helper.onStopNestedScroll(target, type)
        Log.e("onStopNestedScroll", "onStopNestedScroll")
        judgePage(true)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        mScroller.forceFinished(true)
        Log.e("onStartNestedScroll", "onStartNestedScroll")
        moved = false
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.e("onNestedScrollAccepted", "onNestedScrollAccepted")
        helper.onNestedScrollAccepted(child, target, axes, type)
    }

    var moved = false
    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        Log.e("onNestedScroll", "dyConsumed::" + dyConsumed + ",dyUnconsumed::" + dyUnconsumed)
        scrollBy(0, dyUnconsumed)
    }


    /**
     * 松开时布局滑动动画时间
     */
    private var SCROLL_DURATION = 700

    var offscreenPageLimit = 1;
    private var mScroller: Scroller
    private var items = ArrayList<ItemInfo>()
    var adapter: PagerAdapter? = null
        set(value) {
            field = value
            resetLayout()
        }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        Log.e("onInterceptHoverEvent", "...")
        return super.onInterceptHoverEvent(event)
    }

    /**
     * 当更换adapter之后，重置所有信息
     */
    private fun resetLayout() {
        removeAllItem()
        scrollTo(0, 0)
        refreshViews()
    }

    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        mScroller = Scroller(context)
    }

    /**
     * 获取到点击事件的Page 如要是为了防止一次滑动翻超过一页
     */
    private var actionDownPage = 0


    override fun scrollTo(x: Int, y: Int) {
        refreshViews()
        super.scrollTo(x, y)
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
        adapter?.setPrimaryItem(this, getCurrentPage(), itemInfoForPosition(getCurrentPage()).obj)
        adapter?.finishUpdate(this)

    }

    /**
     * 移除所有的View
     */
    private fun removeAllItem() {
        adapter?.startUpdate(this)
        items.forEach {
            adapter?.destroyItem(this, it.position, it.obj)
        }
        items.clear()
        adapter?.finishUpdate(this)
    }

    /**
     * 根据ItemInfo 移除View
     */
    private fun removeForItemInfo(item: ItemInfo) {
        adapter?.destroyItem(this, item.position, item.obj)
        items.remove(item)
    }

    /**
     * 根据位置信息 添加新的view
     */
    private fun addNewView(position: Int) {
        if (position < 0 && position >= adapter!!.count) return
        items.forEach { if (it.position == position) return }
        items.add(ItemInfo(position, adapter!!.instantiateItem(this, position)))
    }

    /**
     * 获取到指定位置的View
     */
    private fun viewForPosition(position: Int): View {
        val item = itemInfoForPosition(position)
        return viewForItemInfo(item)
    }

    private fun viewForItemInfo(itemInfo: ItemInfo): View {
        (0 until childCount).forEach {
            if (adapter!!.isViewFromObject(getChildAt(it), itemInfo.obj)) {
                return getChildAt(it)
            }
        }
        throw NullPointerException()
    }

    private fun itemInfoForPosition(position: Int): ItemInfo {
        items.forEach {
            if (position == it.position) {
                return it
            }
        }
        throw NullPointerException()
    }


    /**
     * 判断应该停留在哪一页
     */
    private fun judgePage(smooth: Boolean) {
        setCurrentItem(getCurrentPage(), smooth)
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
            var itemInfo = itemInfoForView(getChildAt(it))
            getChildAt(it).layout(0, itemInfo.position * height, r - l, (itemInfo.position + 1) * height)
        }
    }

    private fun itemInfoForView(child: View): ItemInfo {
        items.forEach {
            if (adapter!!.isViewFromObject(child, it.obj)) {
                return it
            }
        }
        throw NullPointerException()
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


}