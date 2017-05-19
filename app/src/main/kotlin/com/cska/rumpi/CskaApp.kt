package com.cska.rumpi

import android.provider.Settings
import com.cska.rumpi.utils.StdLibConfig
import com.cska.rumpi.utils.app.StdApp
import com.orhanobut.hawk.Hawk



///////////////////////////////////////////////////////////////////////////
// Patient App
///////////////////////////////////////////////////////////////////////////

class CskaApp : StdApp() {

    override fun onCreate() {
        super.onCreate()
        StdLibConfig.init(
                mainAppId = BuildConfig.APPLICATION_ID,
                buildType = BuildConfig.BUILD_TYPE,
                allowLibDebug = BuildConfig.DEBUG
        )
        Hawk.init(this, Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID))

    }
}
