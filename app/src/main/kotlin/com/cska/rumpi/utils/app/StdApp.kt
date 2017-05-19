package com.cska.rumpi.utils.app

import android.support.multidex.MultiDexApplication
import android.view.ViewConfiguration
import com.jakewharton.threetenabp.AndroidThreeTen
import com.cska.rumpi.utils.Loggable
import com.cska.rumpi.utils.error
import java.lang.reflect.Field


open class StdApp() : MultiDexApplication(), Loggable {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        makeSoftwareMenu()
    }

    private fun makeSoftwareMenu() {
        try {
            val config = ViewConfiguration.get(this)
            val menuKeyField: Field? = ViewConfiguration::class.java.getDeclaredField("sHasPermanentMenuKey")
            menuKeyField?.isAccessible = true
            menuKeyField?.setBoolean(config, false)
        }
        catch(e: Throwable) {
            error("Can't add software menu", e)
        }
    }
}
