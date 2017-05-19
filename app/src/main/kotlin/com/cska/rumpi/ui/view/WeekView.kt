package com.cska.rumpi.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.inflate

/**
 * Created by rumpi on 09.02.2017.
 */

interface OnDaySelect {
    fun selectDay(day: Int)
}

class WeekView : FrameLayout, View.OnClickListener {
    private val day23 by bindView<TextView>(R.id.vw_tv_day23)
    private val day24 by bindView<TextView>(R.id.vw_tv_day24)
    private val day25 by bindView<TextView>(R.id.vw_tv_day25)
    private val day26 by bindView<TextView>(R.id.vw_tv_day26)
    private val day27 by bindView<TextView>(R.id.vw_tv_day27)

    private val dayContainer23 by bindView<View>(R.id.vw_tv_day_container23)
    private val dayContainer24 by bindView<View>(R.id.vw_tv_day_container24)
    private val dayContainer25 by bindView<View>(R.id.vw_tv_day_container25)
    private val dayContainer26 by bindView<View>(R.id.vw_tv_day_container26)
    private val dayContainer27 by bindView<View>(R.id.vw_tv_day_container27)

    private var listener: OnDaySelect? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        this@WeekView.inflate(R.layout.view_week, true)
        dayContainer23.setOnClickListener(this)
        dayContainer24.setOnClickListener(this)
        dayContainer25.setOnClickListener(this)
        dayContainer26.setOnClickListener(this)
        dayContainer27.setOnClickListener(this)
    }

    fun setListener(l: OnDaySelect) {
        listener = l
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.vw_tv_day_container23 -> setDay(23)
            R.id.vw_tv_day_container24 -> setDay(24)
            R.id.vw_tv_day_container25 -> setDay(25)
            R.id.vw_tv_day_container26 -> setDay(26)
            R.id.vw_tv_day_container27 -> setDay(27)
        }
    }

    private fun disableAll() {
        day23?.isEnabled = false
        day24?.isEnabled = false
        day25?.isEnabled = false
        day26?.isEnabled = false
        day27?.isEnabled = false
    }

    fun setDay(day: Int) {
        disableAll()
        when (day) {
            23 -> day23?.isEnabled = true
            24 -> day24?.isEnabled = true
            25 -> day25?.isEnabled = true
            26 -> day26?.isEnabled = true
            27 -> day27?.isEnabled = true
        }
        listener?.selectDay(day)
    }
}
