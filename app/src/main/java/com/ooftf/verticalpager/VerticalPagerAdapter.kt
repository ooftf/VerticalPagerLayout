package com.ooftf.verticalpager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ooftf.verticalpager.fragment.FirstFragment
import com.ooftf.verticalpager.fragment.SecondFragment
import com.ooftf.verticalpager.fragment.ThirdFragment

/**
 * Created by 99474 on 2017/12/26 0026.
 */
class VerticalPagerAdapter(var context: Context, fm :FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
       when(position){
           0-> return FirstFragment()
           2-> return SecondFragment()
           else->return ThirdFragment.newInstance(position)
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