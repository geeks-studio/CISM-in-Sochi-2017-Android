package com.cska.rumpi.ui.base

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.utils.app.Destroyable
import org.jetbrains.anko.findOptional



///////////////////////////////////////////////////////////////////////////
// Patient Nav Header
///////////////////////////////////////////////////////////////////////////

private const val WEATHER_DRAWABLE_PREFIX: String = "weather"

class NavHeader(root: View) : Destroyable {

    private var root: View? = root
    private var textEn = root.findOptional<TextView>(R.id.nhm_tv_en)
    private var textRu = root.findOptional<TextView>(R.id.nhm_tv_ru)

    private var tempText = root.findOptional<TextView>(R.id.nhm_tv_temp)
    private var tempImage = root.findOptional<ImageView>(R.id.nhm_iv_temp)

    fun setLanguage(context: Context, language: String) {
        if(language == "en"){
            textEn?.setTextColor(getColor(context, R.color.text_dark))
            textRu?.setTextColor(getColor(context, R.color.text_dark_not_selected))
        }else{
            textRu?.setTextColor(getColor(context, R.color.text_dark))
            textEn?.setTextColor(getColor(context, R.color.text_dark_not_selected))
        }
    }

    fun setWeather(context: Context, code: Long?, temp: Float?){
        var resId = -1
        if(code == null) resId = R.drawable.img_weather_801
        else {
            resId = when (code.div(100)) {
                2L -> R.drawable.img_weather_2xx
                3L -> R.drawable.img_weather_3xx
                5L -> R.drawable.img_weather_5xx
                6L -> R.drawable.img_weather_6xx
                8L -> R.drawable.img_weather_8xx
                else -> R.drawable.img_weather_801
            }
            if(code == 800L) resId = R.drawable.img_weather_800
        }

        val mIcon = ContextCompat.getDrawable(context, resId)
        DrawableCompat.setTint(mIcon, Color.WHITE);
        tempImage?.setImageDrawable(mIcon)

        tempText?.text = temp?.toInt().toString()
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        textEn?.setOnClickListener(listener)
        textRu?.setOnClickListener(listener)
    }

    override fun destroy() {
        root?.setOnClickListener(null)
        root = null
        textEn = null
        textRu = null
    }
}
