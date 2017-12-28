package com.ooftf.verticalviewpager

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView

/**
 * Created by 99474 on 2017/12/26 0026.
 */
class VerticalViewPagerAdapter(var context: Context) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` //To change body of created functions use File | Settings | File Templates.
    }

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var cach: MutableMap<Int, View> = HashMap()
    override fun getCount(): Int {
        return 5
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recycler = cach[position]
        val view = if (recycler == null) {
            val view = inflater.inflate(R.layout.page_scroll, container, false)
            val webView = view.findViewById<WebView>(R.id.webView)
            webView.settings.javaScriptEnabled = true
            when (position) {
                0 -> {
                    webView.loadUrl("https://github.com/marketplace")
                }
                1 -> {
                    webView.loadUrl("https://www.kotlincn.net/")
                }
                2 -> {
                    webView.loadUrl("https://developer.android.google.cn/index.html")
                }
                3 -> {
                    webView.loadUrl("https://m.sogou.com/?fr=s-sogou&clk=s-sogou&prs=8&rfh=1")
                }
                else -> {
                    webView.loadUrl("https://m.bilibili.com/index.html")
                }
            }
            view.findViewById<TextView>(R.id.textView).setText("第${position}个Item")
            view
        } else {
            Log.e("instantiateItem", "复用")
            cach.remove(position)
            recycler
        }

        container?.addView(view)
        return view
    }
    fun buildContent(s:String):String{
        var result=""
        for (i in 0..200){
            result+=s
        }
        return result
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?);
        cach.put(position, `object` as View)
    }
}