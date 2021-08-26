package com.example.appsell.base

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtilCallback<T>(private val oldList: List<T>,
                                       private val newList: List<T>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    fun getOldItem(position: Int) = oldList[position]

    fun getNewItem(position: Int) = newList[position]

}