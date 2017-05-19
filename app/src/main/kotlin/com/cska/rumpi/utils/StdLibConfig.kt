package com.cska.rumpi.utils

import com.cska.rumpi.BuildConfig

///////////////////////////////////////////////////////////////////////////
// Std Lib Config
///////////////////////////////////////////////////////////////////////////

object StdLibConfig {
    var mainAppId = BuildConfig.APPLICATION_ID
        private set
    var buildType = BuildConfig.BUILD_TYPE
        private set

    var allowLibDebug = BuildConfig.DEBUG
        private set

    @Suppress("NAME_SHADOWING")
    fun init(
            mainAppId: String,
            buildType: String,
            allowLibDebug: Boolean
    ): StdLibConfig =
            apply {
                this.mainAppId = mainAppId
                this.buildType = buildType
                this.allowLibDebug = allowLibDebug
            }
}
