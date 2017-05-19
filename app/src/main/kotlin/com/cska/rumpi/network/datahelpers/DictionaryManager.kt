package com.cska.rumpi.network.datahelpers

import com.cska.rumpi.network.models.ContestsModel
import com.cska.rumpi.network.models.EventModel
import com.cska.rumpi.network.models.ObjectModel
import com.cska.rumpi.network.models.ScheduleListResponseModel
import com.cska.rumpi.utils.Prefs
import com.cska.rumpi.utils.isNotNullOrEmpty

/**
 * Created by rumpi on 09.02.2017.
 */

object DictionaryManager {

    operator fun get(field: String, defaultValue: String): String {
        return Prefs.getValue(field, defaultValue)
    }

    fun store(field: String, value: String) {
        Prefs.store(field, value)
    }

    fun storeSchedule(language: String, result: List<ScheduleListResponseModel>) {
        val array23 = result.filter { it.day == "2017-02-23" }
        val array24 = result.filter { it.day == "2017-02-24" }
        val array25 = result.filter { it.day == "2017-02-25" }
        val array26 = result.filter { it.day == "2017-02-26" }
        val array27 = result.filter { it.day == "2017-02-27" }

        if (array23.isNotNullOrEmpty()) Prefs.storeScheduleList(language + "23", array23.first().items)
        if (array24.isNotNullOrEmpty()) Prefs.storeScheduleList(language + "24", array24.first().items)
        if (array25.isNotNullOrEmpty()) Prefs.storeScheduleList(language + "25", array25.first().items)
        if (array26.isNotNullOrEmpty()) Prefs.storeScheduleList(language + "26", array26.first().items)
        if (array27.isNotNullOrEmpty()) Prefs.storeScheduleList(language + "27", array27.first().items)
    }

    fun getSchedule(language: String, day: String) =
            Prefs.getScheduleList(language + day)

    fun storeContests(language: String, result: List<ContestsModel>?, id: Long) {
        if (result.isNotNullOrEmpty()) Prefs.storeIdNameList(language + id.toString(), result!!)
    }

    fun getContests(language: String, id: Long) =
            Prefs.getIdNameList(language + id.toString())

    fun storeEvents(language: String, result: List<EventModel>?) {
        if (result.isNotNullOrEmpty()) {
            for (item in result!!) {
                storeContests(language, item.contests, item.id)
                item.contests = null
            }
            Prefs.storeIdNameList(language + "events", result)
        }
    }

    fun getEvents(language: String) =
            Prefs.getIdNameList(language + "events")

    fun storeObjects(language: String, result: List<ObjectModel>?) {
        if (result.isNotNullOrEmpty()) Prefs.storeObjectsList(language + "objects", result!!)
    }

    fun getObjects(language: String) =
            Prefs.getObjectsList(language + "objects")

    operator fun get(field: String, defaultValue: Int): Int {
        return Prefs.getValue(field, defaultValue)
    }

    fun store(field: String, value: Int) {
        Prefs.store(field, value)
    }
}
