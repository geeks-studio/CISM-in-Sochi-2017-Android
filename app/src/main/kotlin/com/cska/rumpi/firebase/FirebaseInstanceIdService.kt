package com.cska.rumpi.firebase

import android.provider.Settings
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.Prefs
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by rumpi on 02.02.2017.
 */

class CskaFirebaseInstanceIdService : FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token;
        Prefs.storeToken(refreshedToken?: "")
        val id = Settings.Secure.getString(applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID);
        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        RestApi().sendToken(id, refreshedToken?: "", language)
                .map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }
}
