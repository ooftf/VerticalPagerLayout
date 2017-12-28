package com.ooftf.verticalpager.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ooftf.verticalpager.R
import com.ooftf.verticalpager.SpialeAdapter
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * Created by 99474 on 2017/12/28 0028.
 */
class SecondFragment : Fragment() {
    private lateinit var adapter: SpialeAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("onCreateView","container"+container.toString());
        var view = inflater.inflate(R.layout.fragment_second,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = object : RecyclerView.Adapter<ViewHolder>(){
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.textView.setText("占位符数据::$position")
                Glide.with(holder.image).load("https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/s7.png").into(holder.image)
            }

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_spiale, parent, false))
            }

            override fun getItemCount(): Int {
               return 20;
            }

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView:TextView
        var image:ImageView
        init {
            textView = itemView.findViewById(R.id.textView)
            image = itemView.findViewById(R.id.imageView)
        }
    }
}