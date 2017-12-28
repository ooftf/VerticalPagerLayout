package com.ooftf.verticalviewpager

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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
class VerticalViewPagerFragmentAdapter(var context: Context,fm :FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
       when(position){
           0-> return FirstFragment()
           1-> return SecondFragment()
           else->return FirstFragment()
       }
    }

 /*   override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` //To change body of created functions use File | Settings | File Templates.
    }*/

//    private var inflater: LayoutInflater = LayoutInflater.from(context)
    //private var cach: MutableMap<Int, View> = HashMap()
   /* override fun getCount(): Int {
        return 2
    }*/
   /* override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?);
        //cach.put(position, `object` as View)
    }*/
}