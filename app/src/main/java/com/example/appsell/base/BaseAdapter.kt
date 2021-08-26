package com.example.appsell.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseAdapter<T, VH : BaseAdapter.ViewHolder>(protected val context: Context) :
        RecyclerView.Adapter<VH>(), Filterable, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val inflater = LayoutInflater.from(context)

    var originData = ArrayList<T>()
        private set
    var data = ArrayList<T>()
        private set

    private lateinit var onItemClicked: (position: Int) -> Unit
    private lateinit var onDataFiltered: () -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView =  inflater.inflate(getItemViewId(viewType), parent, false)
        return getViewHolder(viewType, itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (isRegisterListeners()) {
            registerListeners(holder)
        }
    }

    override fun getFilter(): Filter? = null

    fun getItem(position: Int) = data[position]

    fun submitList(list: List<T>, updateOriginData: Boolean = false) {
        if (updateOriginData) {
            originData = list as ArrayList<T>
        }
        data = list as ArrayList<T>
        notifyDataSetChanged()
    }

    fun submitList(list: List<T>, updateOriginData: Boolean, diffUtilCallback: DiffUtil.Callback) {
        if (updateOriginData) {
            originData = list as ArrayList<T>
        }
        data = list as ArrayList<T>

        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addItem(item: T, updateOriginData: Boolean = false) {
        if (updateOriginData && originData !== data) {
            originData.add(item)
        }
        data.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun setOnItemClickListener(onItemClicked: (position: Int) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun setOnDataFilterListener(onDataFiltered: () -> Unit) {
        this.onDataFiltered = onDataFiltered
    }

    fun invokeOnItemClicked(position: Int) {
        if (::onItemClicked.isInitialized) {
            onItemClicked.invoke(position)
        }
    }

    fun invokeOnDataFiltered() {
        if (::onDataFiltered.isInitialized) {
            onDataFiltered.invoke()
        }
    }

    fun setList(list: List<T>) {
        originData.clear()
        originData.addAll(list)

        data.clear()
        data.addAll(list)
    }

    fun updateList(list: List<T>, updateOriginData: Boolean) {
        val diffUtilCallback = getDiffUtilCallback(list) ?: return
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        if (updateOriginData) {
            originData.clear()
            originData.addAll(list)
        }
        data.clear()
        data.addAll(list)

        diffResult.dispatchUpdatesTo(this)
    }

    open fun getDiffUtilCallback(list: List<T>): BaseDiffUtilCallback<T>? = null

    abstract fun getItemViewId(viewType: Int): Int

    abstract fun getViewHolder(viewType: Int, itemView: View): VH

    open fun isRegisterListeners() = true

    open fun registerListeners(holder: VH) {
        holder.itemView.setOnClickListener {
            invokeOnItemClicked(holder.adapterPosition)
        }
    }

    // ---------------------------------------------------------------------------------------------
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), CoroutineScope {
        protected val context = itemView.context!!

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main
    }

}