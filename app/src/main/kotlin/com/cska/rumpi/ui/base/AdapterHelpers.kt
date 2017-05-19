package com.cska.rumpi.ui.base

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.cska.rumpi.R
import com.cska.rumpi.utils.inflate
import java.util.ArrayList

/**
 * Created by rumpi on 05.02.2017.
 */

object AdapterConstants {
    val NEWS = 1
    val LOADING = 2
    val OBJECTS = 3
    val SCHADULE = 4
    val RESULT = 5
}

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            item: ViewType,
            next: ViewType?,
            listener: OnViewTypeClickListener?) {

    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_loading)){
    }
}

interface ViewType{
    fun getViewType(): Int
}

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    fun onBindViewHolder(holder : RecyclerView.ViewHolder,
                         item : ViewType,
                         next :ViewType? = null,
                         listener: OnViewTypeClickListener?)
}

interface OnViewTypeClickListener{
    fun onItemClick(id: Long)
}

abstract class BaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    protected var items: ArrayList<ViewType> = ArrayList()
    protected var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    protected var listener : OnViewTypeClickListener? = null
    override fun getItemCount() : Int{
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val next = if(this.items.size < 1 || position >=this.items.size-1) null else this.items[position+1]
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position], next, listener)
    }

    override fun getItemViewType(position: Int) : Int{
        return this.items.get(position).getViewType()
    }

    protected fun getLastPosition() = if(items.lastIndex == -1) 0 else items.lastIndex

    fun setOnClickItemListener(l : OnViewTypeClickListener){
        listener = l
    }
}
