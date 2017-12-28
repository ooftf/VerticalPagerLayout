package com.ooftf.verticalviewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by 99474 on 2017/12/23 0023.
 */
class SpialeAdapter(var context: Context) : BaseAdapter() {
    var inflater = LayoutInflater.from(context)
    var list = ArrayList<SpialeAdapterBean>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView ?: inflater.inflate(R.layout.item_spiale, parent, false)
        Glide.with(context).load(getItem(position).imageUrl).into(itemView.findViewById<ImageView>(R.id.imageView))
        itemView.findViewById<TextView>(R.id.textView).text = getItem(position).text
        return itemView
    }

    override fun getItem(position: Int): SpialeAdapterBean {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}