package com.cska.rumpi.network.models

import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.ViewType
import java.util.ArrayList

/**
 * Created by rumpi on 09.02.2017.
 */

///////////////////////////////////////////////////////////////////////////
// Schedule Response Models
///////////////////////////////////////////////////////////////////////////

class ScheduleListResponseModel {
    var day: String? = null
    var items: List<ScheduleItemModel> = ArrayList()
}

class ScheduleItemModel: ViewType {
    var id: Long =  3
    var start: String = ""
    var end: String = ""
    var object_name: String = ""
    var game_object_id: Long = -1
    var event_id: Long = -1L
    var contest_id: Long = -1L
    var event: Boolean= false
    var name: String? = null
    var day: String? = null
    var event_name: String? = null

    override fun getViewType() = AdapterConstants.SCHADULE
}