package com.ooftf.verticalpager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ooftf.vertical.ScrollEdgeEngine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verticalViewPager.adapter = VerticalPagerAdapter(this, supportFragmentManager)
        verticalViewPager.setScrollEdgeAnalyzer { i, view ->
            when (i) {
                0 -> ScrollEdgeEngine(view.findViewById(R.id.scrollView))
                1 -> ScrollEdgeEngine(view.findViewById(R.id.recyclerView))
                2 -> ScrollEdgeEngine(view)
                else -> ScrollEdgeEngine(view)
            }
        }
        verticalViewPager.setCurrentItem(3)
    }
}
