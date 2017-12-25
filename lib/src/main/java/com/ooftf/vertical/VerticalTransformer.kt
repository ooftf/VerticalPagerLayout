package com.ooftf.vertical

import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * Created by 99474 on 2017/12/24 0024.
 */

class VerticalTransformer : ViewPager.PageTransformer {
    lateinit var showing: View
    override fun transformPage(page: View, position: Float) {
        page.translationX = page.width * -position
        val yPosition = position * page.height
        page.translationY = yPosition
        if (position == 0f) {
            showing = page
        }
    }
}
