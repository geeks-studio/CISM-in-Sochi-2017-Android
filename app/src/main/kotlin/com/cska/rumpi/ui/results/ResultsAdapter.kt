package com.cska.rumpi.ui.results

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.models.ResultModel
import com.cska.rumpi.network.models.ScheduleItemModel
import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.BaseAdapter
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.base.ViewType
import com.cska.rumpi.ui.base.ViewTypeDelegateAdapter
import com.cska.rumpi.ui.schedule.ScheduleViewHolder
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.fromFullString
import com.cska.rumpi.utils.getDrawableByName
import com.cska.rumpi.utils.inflate
import com.cska.rumpi.utils.toDayDateString
import com.cska.rumpi.utils.toEventString
import java.util.Date

/**
 * Created by rumpi on 13.02.2017.
 */

class ResultsAdapter : BaseAdapter(){

    init{
        delegateAdapters.put(AdapterConstants.RESULT, ResultsDelegateAdapter())
        delegateAdapters.put(AdapterConstants.SCHADULE, ScheduleContestDelegateAdapter())
    }

    fun clearAndAdd(news: List<ViewType>){
        items.clear()
        items.addAll(news)
        notifyDataSetChanged()
    }
}

class ResultsDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder{
        return ObjectViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  item: ViewType,
                                  next: ViewType?,
                                  listener: OnViewTypeClickListener?) {
        holder as ObjectViewHolder
        holder.bind(item as ResultModel)
    }

    class ObjectViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_result)){

        private val imageView by bindView<ImageView>(R.id.ir_iv_country)
        private val nameView by bindView<TextView>(R.id.ir_tv_name)
        private val placeView by bindView<TextView>(R.id.ir_tv_place)
        private val scoreView by bindView<TextView>(R.id.ir_tv_score)

        fun bind(item: ResultModel) = with(itemView){
            nameView.text = item.name
            placeView.text = item.place.toString()
            scoreView.text = item.points
            val drawable = context.getDrawableByName("ic_"+ item.country)
            if(drawable != null) imageView.setImageDrawable(drawable)
        }
    }

}


class ScheduleContestDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder{
        return ScheduleContestViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  item: ViewType,
                                  next: ViewType?,
                                  listener: OnViewTypeClickListener?) {
        holder as ScheduleContestViewHolder
        holder.bind(item as ScheduleItemModel, next == null, listener)
    }
}

class ScheduleContestViewHolder(parent: ViewGroup) : ScheduleViewHolder(parent){

    override fun bind(item: ScheduleItemModel, isLast: Boolean, listener: OnViewTypeClickListener?) {
        val view = super.bind(item, isLast, listener)

        timeView.text = Date().fromFullString(item.start).toDayDateString() +
                        "   " + Date().fromFullString(item.start).toEventString() +
                        " - " + Date().fromFullString(item.end).toEventString()

        return view
    }

}