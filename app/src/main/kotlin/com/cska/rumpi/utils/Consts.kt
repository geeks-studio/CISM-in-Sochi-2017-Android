package com.cska.rumpi.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * Created by rumpi on 26.01.2017.
 */

object Consts {
    const val PREF_LANGUAGE = "pref_language"
    const val PREF_NAME = "CISM_preference"


    fun setLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale)
        }
        else {
            setSystemLocaleLegacy(config, locale)
        }
        context.resources.updateConfiguration(config,
                context.resources.displayMetrics)
    }

    fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }
}
