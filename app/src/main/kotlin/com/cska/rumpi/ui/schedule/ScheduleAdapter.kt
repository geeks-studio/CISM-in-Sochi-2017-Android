package com.cska.rumpi.ui.schedule

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.models.ObjectModel
import com.cska.rumpi.network.models.ScheduleItemModel
import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.BaseAdapter
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.base.ViewType
import com.cska.rumpi.ui.base.ViewTypeDelegateAdapter
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.fromFullString
import com.cska.rumpi.utils.inflate
import com.cska.rumpi.utils.isInvisible
import com.cska.rumpi.utils.toEventString
import java.util.Date

/**
 * Created by rumpi on 09.02.2017.
 */

class ScheduleAdapter : BaseAdapter(){

    init{
        delegateAdapters.put(AdapterConstants.SCHADULE, ScheduleDelegateAdapter())
    }

    fun clearAndAdd(news: List<ScheduleItemModel>){
        items.clear()
        items.addAll(news)
        notifyDataSetChanged()
    }

    fun getObjects(): List<ScheduleItemModel>{
        return items
                .filter { it.getViewType() == AdapterConstants.SCHADULE }
                .map { it as ScheduleItemModel }
    }

    fun getObjectById(id: Long) = items.filter { (it as ObjectModel).id == id }[0]
}

class ScheduleDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder{
        return ScheduleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  item: ViewType,
                                  next: ViewType?,
                                  listener: OnViewTypeClickListener?) {
        holder as ScheduleViewHolder
        holder.bind(item as ScheduleItemModel, next == null, listener)
    }
}

open class ScheduleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.item_schedule)) {

    private val imageView by bindView<ImageView>(R.id.is_iv_image)
    private val titleView by bindView<TextView>(R.id.is_txt_name)
    protected val timeView by bindView<TextView>(R.id.is_txt_time)
    private val placeView by bindView<TextView>(R.id.is_txt_place)
    private val bottomLine by bindView<View>(R.id.is_bottom_line)
    private val container by bindView<View>(R.id.schedule_container)

    open fun bind(item: ScheduleItemModel, isLast: Boolean, listener: OnViewTypeClickListener?) = with(itemView) {
        placeView.text = item.object_name
        container.setOnClickListener({ view -> listener?.onItemClick(item.game_object_id) })
        titleView.text = item.event_name + ": " + item.name
        timeView.text = Date().fromFullString(item.start).toEventString() + " - " + Date().fromFullString(item.end).toEventString()
        timeView.setBackgroundColor(ContextCompat.getColor(context, getBackground(item.event_id)))
        if (item.event) {
            imageView.setImageResource(R.drawable.ic_star_icon_v3)
        }
        else {
            imageView.setImageResource(getResBySport(item.event_id))
        }
        bottomLine.isInvisible = isLast
    }

    fun getResBySport(id: Long) = when (id) {
        1L -> R.drawable.ic_img_sport_2
        3L -> R.drawable.ic_img_sport_3
        4L -> R.drawable.ic_img_sport_5
        5L -> R.drawable.ic_img_sport_4
        6L -> R.drawable.ic_img_sport_7
        7L -> R.drawable.ic_img_sport_1
        8L -> R.drawable.ic_img_sport_6
        9L -> R.drawable.ic_medal
        else -> R.drawable.ic_star_icon_v3
    }

    fun getBackground(id: Long) = when (id) {
        10L, 11L, 12L -> R.color.colorAccent
        9L -> R.color.colorYellow
        else -> R.color.colorPrimaryDark
    }
}