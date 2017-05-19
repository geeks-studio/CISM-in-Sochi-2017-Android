package com.cska.rumpi.ui.schedule

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.objects.ObjectActivity
import com.cska.rumpi.ui.view.OnDaySelect
import com.cska.rumpi.ui.view.WeekView
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import java.util.Calendar


/**
 * Created by rumpi on 01.02.2017.
 */

class ScheduleFragment: BaseFragment(),
                        OnDaySelect,
                        OnViewTypeClickListener {
    override val titleResId: Int get() = R.string.label_schedule
    override val layoutResId: Int get() = R.layout.fragment_shedule

    private val scheduleList by bindView<RecyclerView>(R.id.fs_rv_schedule)
    private val week by bindView<WeekView>(R.id.fs_wv_week)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleList.setHasFixedSize(true)
        scheduleList.layoutManager = LinearLayoutManager(context)
        initAdapter()
        week.setListener(this)

        val date = Calendar.getInstance()
        if(date.get(Calendar.YEAR) == 2017
           && date.get(Calendar.MONTH) == 2
           && date.get(Calendar.DAY_OF_MONTH) >= 23
           && date.get(Calendar.DAY_OF_MONTH) <= 27){
            week.setDay(date.get(Calendar.DAY_OF_MONTH))
        }else{
            week.setDay(23)
        }
    }

    private fun initAdapter(){
        if(scheduleList.adapter == null){
            scheduleList.adapter = ScheduleAdapter()
            (scheduleList.adapter as ScheduleAdapter).setOnClickItemListener(this)
        }
    }

    override fun selectDay(day: Int) {
        val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        (scheduleList.adapter as ScheduleAdapter).clearAndAdd(DictionaryManager.getSchedule(language, day.toString()))

    }

    override fun onItemClick(id: Long) {
        ObjectActivity.launch(activity, id)
    }
}