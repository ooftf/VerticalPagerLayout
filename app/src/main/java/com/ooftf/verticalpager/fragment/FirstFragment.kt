package com.ooftf.verticalpager.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ooftf.verticalpager.R
import com.ooftf.verticalpager.SpialeAdapter
import com.ooftf.verticalpager.SpialeAdapterBean
import com.youth.banner.loader.ImageLoaderInterface
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * Created by 99474 on 2017/12/28 0028.
 */
class FirstFragment: Fragment() {
    private lateinit var adapter: SpialeAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("onCreateView","container"+container.toString());
        var view = inflater.inflate(R.layout.fragment_first,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupSpialeLayout()

    }
    private fun setupBanner() {
        banner.setImageLoader(object :ImageLoaderInterface<ImageView>{
            override fun createImageView(context: Context?): ImageView {
                return ImageView(context)
            }

            override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
                Glide.with(context).load(path).into(imageView)
            }

        })
        var list = ArrayList<String>()
        list.add("https://camo.githubusercontent.com/fa591b0ea9768e3722fcd690cc97f987867573d9/687474703a2f2f6f63656835316b6b752e626b742e636c6f7564646e2e636f6d2f62616e6e65725f6578616d706c65332e706e67")
        list.add("https://camo.githubusercontent.com/72e7034bb9f3ed5e4d28c25a94adb22bb9e7cb98/687474703a2f2f6f63656835316b6b752e626b742e636c6f7564646e2e636f6d2f62616e6e65725f6578616d706c65312e706e67")
        list.add("https://camo.githubusercontent.com/078504c5723b59c8ebe787a059853fa1a603a381/687474703a2f2f6f63656835316b6b752e626b742e636c6f7564646e2e636f6d2f62616e6e65725f6578616d706c65322e706e67")
        list.add("https://camo.githubusercontent.com/44eeb7b3a25f1d34aa6d2ff7dd62c8f07af3b560/687474703a2f2f6f63656835316b6b752e626b742e636c6f7564646e2e636f6d2f62616e6e65725f6578616d706c65342e706e67")
        banner.setImages(list)
        banner.start()
    }
    private fun setupSpialeLayout() {
        adapter = SpialeAdapter(context!!)
        spialeLayout.adapter = adapter
        spialeLayout.setOnItemClickListener { position, _, itemData ->
            itemData as SpialeAdapterBean
            Toast.makeText(context, "$position :: ${itemData.text}", Toast.LENGTH_SHORT).show()
        }
        adapter.list.add(SpialeAdapterBean("item-0 ", "https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/s7.png"))
        adapter.list.add(SpialeAdapterBean("item-1 ", "https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/logo_empty.png"))
        adapter.list.add(SpialeAdapterBean("item-2 ", "https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/logo_full.png"))
        adapter.list.add(SpialeAdapterBean("item-3 ", "https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/logo_legacy.png"))
        adapter.list.add(SpialeAdapterBean("item-4 ", "https://github.com/ooftf/SpialeLayout/raw/master/ImageRepository/logo_orb.png"))
        adapter.notifyDataSetChanged()
    }
}