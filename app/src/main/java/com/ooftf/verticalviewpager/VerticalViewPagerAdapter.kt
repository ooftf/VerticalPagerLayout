package com.ooftf.verticalviewpager

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by 99474 on 2017/12/26 0026.
 */
class VerticalViewPagerAdapter(var context:Context) : PagerAdapter() {
    private var inflater:LayoutInflater = LayoutInflater.from(context)
    private var cach:MutableMap<Int,View> = HashMap()
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return  view == `object`
    }

    override fun getCount(): Int {
        return 5
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val recycler = cach[position]
        val view = if (recycler == null){
            val view = inflater.inflate(R.layout.page_scroll,container,false)
            val child = view.findViewById<View>(R.id.view)
            val button = view.findViewById<View>(R.id.button)
            button.setOnClickListener {
                Toast.makeText(context,"$position",Toast.LENGTH_SHORT).show()
            }
            when(position){
                0->child.setBackgroundColor(Color.parseColor("#00FFFF"))
                1->{child.setBackgroundColor(Color.parseColor("#FF00FF"))
                    child.layoutParams.height = 500}
                2->child.setBackgroundColor(Color.parseColor("#FFFF00"))
                3->child.setBackgroundColor(Color.parseColor("#0000FF"))
                else->child.setBackgroundColor(Color.parseColor("#FF0000"))
            }
            view
        }else{
            Log.e("instantiateItem","复用")
            cach.remove(position)
            recycler
        }

        container?.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View?);
        cach.put(position, `object` as View)
    }
}