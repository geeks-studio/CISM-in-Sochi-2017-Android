package com.cska.rumpi.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.R
import com.cska.rumpi.utils.Consts
import org.jetbrains.anko.intentFor


/**
 * Created by rumpi on 26.01.2017.
 */

class LanguageActivity : AppCompatActivity(),
                         View.OnClickListener {
    companion object {
        fun launch(activity: Activity) {
            val intent = activity.intentFor<LanguageActivity>()
            activity.startActivity(intent)
        }
    }

    private val selectRu by bindView<FrameLayout>(R.id.al_select_ru)
    private val selectEn by bindView<FrameLayout>(R.id.al_select_en)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        selectEn.setOnClickListener(this)
        selectRu.setOnClickListener(this)
    }

    fun selectLanguage(position: Int) {
        val sharedPref = getApplicationContext().getSharedPreferences(Consts.PREF_NAME, 0);
        val editor = sharedPref.edit()

        when (position) {
            0 -> {
                editor.putString(Consts.PREF_LANGUAGE, "ru")
                Consts.setLanguage(baseContext, "ru")
            }
            1 -> {
                editor.putString(Consts.PREF_LANGUAGE, "en")
                Consts.setLanguage(baseContext, "en")
            }
        }
        editor.apply()
        HomeActivity.launch(this)
        finish()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.al_select_ru -> selectLanguage(0)
            R.id.al_select_en -> selectLanguage(1)
        }
    }
}