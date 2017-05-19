package com.cska.rumpi.utils

import com.cska.rumpi.network.models.IdNameModel
import com.cska.rumpi.network.models.ObjectModel
import com.cska.rumpi.network.models.ScheduleItemModel
import com.orhanobut.hawk.Hawk
import java.util.ArrayList

/**
 * Created by rumpi on 09.02.2017.
 */

object Prefs {
    private val PREF_STATUS = "Status"

    private val TOKEN = "token"

    fun storeToken(token: String) {
        Hawk.put(TOKEN, token)
    }

    fun readToken(): String {
        return Hawk.get(TOKEN, "")
    }

    var search: Int
        get() = Hawk.get(PREF_STATUS, 100)
        set(status) {
            Hawk.put(PREF_STATUS, status)
        }

    fun store(field: String, value: String) {
        Hawk.put(field, value)

    }

    fun store(field: String, value: Int) {
        Hawk.put(field, value)

    }


    fun storeScheduleList(field: String, data: List<ScheduleItemModel>) {
        Hawk.put<ScheduleItemModel>(field, data)

    }

    fun getScheduleList(field: String): List<ScheduleItemModel> {
        return Hawk.get(field, ArrayList<ScheduleItemModel>())
    }

    fun storeObjectsList(field: String, data: List<ObjectModel>) {
        Hawk.put<ObjectModel>(field, data)

    }

    fun getObjectsList(field: String): List<ObjectModel> {
        return Hawk.get(field, ArrayList<ObjectModel>())
    }

    fun storeIdNameList(field: String, data: List<IdNameModel>) {
        Hawk.put<IdNameModel>(field, data)

    }

    fun getIdNameList(field: String): List<IdNameModel> {
        return Hawk.get(field, ArrayList<IdNameModel>())
    }


    fun getValue(field: String, defaultValue: String): String {
        return Hawk.get(field, defaultValue)

    }

    fun getValue(field: String, defaultValue: Int): Int {
        return Hawk.get(field, defaultValue)

    }

}
