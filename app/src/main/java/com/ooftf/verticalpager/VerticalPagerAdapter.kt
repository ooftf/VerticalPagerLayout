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
        return when(position){
            0-> FirstFragment()
            2-> SecondFragment()
            else-> ThirdFragment.newInstance(position)
        }
    }
}