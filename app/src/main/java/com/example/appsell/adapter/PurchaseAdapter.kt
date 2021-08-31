package com.example.appsell.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.appsell.R
import com.example.appsell.base.BaseAdapter
import com.example.appsell.base.Constant
import com.example.appsell.base.Until
import com.example.appsell.model.Purchase
import kotlinx.android.synthetic.main.item_purchase.view.*

class PurchaseAdapter(context: Context) : BaseAdapter<Purchase, PurchaseAdapter.ViewHolder>(context) {

    override fun getItemViewId(viewType: Int): Int {
        return R.layout.item_purchase
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindView(position)
    }

    override fun getViewHolder(viewType: Int, itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }


    inner class ViewHolder(itemView: View) : BaseAdapter.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bindView(position: Int) {
            val data = getItem(position)
            var cost: Long = 0
            data.orders?.forEach {
                cost += it.count * it.product!!.cost
            }
            itemView.txtCost.text = "$cost VND"
            itemView.txtDate.text = Until.formatDate(data.date)
            itemView.txtUserName.text = data.userOrder?.userName

            when (data.statusOrder) {
                Constant.ODER -> {
                    itemView.txtType.text = "Đang xác nhận"
                    itemView.txtType.setTextColor(context.getColor(R.color.colorPrimary))
                }

                Constant.SELL -> {
                    itemView.txtType.text = "Đã xác nhận"
                    itemView.txtType.setTextColor(context.getColor(R.color.textColorRedCrimson))
                }
            }
        }
    }

}