package com.ooftf.verticalpager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_third.*
import java.text.FieldPosition

/**
 * Created by 99474 on 2017/12/28 0028.
 */
class ThirdFragment(): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_third,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var position = arguments?.getInt(ARGUMENT_POSITION)?:0
        textView.text = "第${position}个Item"
    }
    companion object {
        val ARGUMENT_POSITION = "argument_position"
        fun newInstance(position: Int):ThirdFragment{
            var fragment = ThirdFragment()
            var bundle = Bundle()
            bundle.putInt(ARGUMENT_POSITION,position)
            fragment.arguments = bundle
            return fragment
        }
    }
}