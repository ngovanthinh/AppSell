package com.example.appsell.adapter

import android.content.Context
import android.view.View
import com.example.appsell.R
import com.example.appsell.base.BaseAdapter
import com.example.appsell.model.MenuProduct
import kotlinx.android.synthetic.main.item_menu_product.view.*

class MenuProductAdapter(context: Context) : BaseAdapter<MenuProduct, MenuProductAdapter.ViewHolder>(context) {

    override fun getItemViewId(viewType: Int): Int = R.layout.item_menu_product

    override fun getViewHolder(viewType: Int, itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindView(position)
    }

    inner class ViewHolder(itemView: View) : BaseAdapter.ViewHolder(itemView) {
        fun bindView(position: Int) {
            val data = getItem(position)
            itemView.txt_type_product.text = data.type
            var id =  R.drawable.ic_vegetables
            when (position) {
                0 -> id = R.drawable.ic_vegetables
                1 -> id = R.drawable.ic_pakage
                2 -> id = R.drawable.ic_beef
                3 -> id = R.drawable.ic_different
            }

            itemView.imgPart.setImageDrawable(context.getDrawable(id))
        }
    }
}