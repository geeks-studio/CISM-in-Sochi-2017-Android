package com.cska.rumpi.ui.objects

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.models.ObjectModel
import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.BaseAdapter
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.base.ViewType
import com.cska.rumpi.ui.base.ViewTypeDelegateAdapter
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.inflate

/**
 * Created by rumpi on 05.02.2017.
 */

class ObjectsAdapter : BaseAdapter(){

    init{
        delegateAdapters.put(AdapterConstants.OBJECTS, ObjectsDelegateAdapter())
    }

    fun clearAndAdd(news: List<ObjectModel>){
        items.clear()
        items.addAll(news)
        notifyDataSetChanged()
    }

    fun getObjects(): List<ObjectModel>{
        return items
                .filter { it.getViewType() == AdapterConstants.OBJECTS }
                .map { it as ObjectModel }
    }

    fun getObjectById(id: Long) = items.filter { (it as ObjectModel).id == id }[0]
}

class ObjectsDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder{
        return ObjectViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  item: ViewType,
                                  next: ViewType?,
                                  listener: OnViewTypeClickListener?) {
        holder as ObjectViewHolder
        holder.bind(item as ObjectModel, listener)
    }

    class ObjectViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_object)){

        private val rootContainer by bindView<View>(R.id.io_root_view)
        private val imageView by bindView<ImageView>(R.id.io_iv_image)
        private val titleView by bindView<TextView>(R.id.io_tv_title)
        private val typeView by bindView<TextView>(R.id.io_tv_type)
        private val happenedView by bindView<TextView>(R.id.io_tv_happened)
        private val capacityView by bindView<TextView>(R.id.io_tv_capacity)

        fun bind(item: ObjectModel, listener: OnViewTypeClickListener?) = with(itemView){
            val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
            val language = sharedPref.getString(Consts.PREF_LANGUAGE, "none")
            rootContainer.setOnClickListener({ view ->  listener?.onItemClick(item.id)})
            setImage(item.id)
            titleView.text = "\"" + item.title + "\""
            typeView.text = item.object_type
            capacityView.text = when(language){
                "ru" -> resources.getQuantityString(R.plurals.label_ru_capacity, item.capacity, item.capacity)
                else -> resources.getQuantityString(R.plurals.label_en_capacity, item.capacity, item.capacity)
            }
            happenedView.text = item.now
        }

        fun setImage(id: Long){
            when(id){
                1L -> imageView.setImageResource(R.drawable.ic_object_1)
                2L -> imageView.setImageResource(R.drawable.ic_object_2)
                3L -> imageView.setImageResource(R.drawable.ic_object_3)
                4L -> imageView.setImageResource(R.drawable.ic_object_4)
                5L -> imageView.setImageResource(R.drawable.ic_object_5)
            }
        }
    }

}
