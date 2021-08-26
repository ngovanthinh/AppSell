package com.example.appsell.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appsell.R
import com.example.appsell.model.SliderItem
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_slider.view.*


/**
 * Created by ThinhNV on 23/08/2021.
 */
class SliderAdapter(private val context: Context, private val sliderItems: List<SliderItem>) :
    SliderViewAdapter<SliderAdapter.ViewHolderSlider>() {

    override fun getCount(): Int = sliderItems.count()

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolderSlider {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.item_slider, null)

        return ViewHolderSlider(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolderSlider?, position: Int) {
        val sliderItem: SliderItem = sliderItems[position]
        Glide.with(viewHolder!!.itemView)
            .load(sliderItem.image)
            .fitCenter()
            .into(viewHolder.itemView.iv_auto_image_slider)
    }

    inner class ViewHolderSlider(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

    }
}