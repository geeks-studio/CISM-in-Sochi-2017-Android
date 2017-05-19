package com.cska.rumpi.network.models

import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.ViewType
import java.util.ArrayList

class MainResponse(
    val errors: Boolean,
    val error_message: String
)

class WhetherResponse{
    var errors: Boolean = false
    var weather: WhetherModel? = null
}

class WhetherModel{
    var id: Long = -1
    var main: String? = null
    var description: String? = null
    var icon: String? = null
    var temperature: Float = 0F
}

///////////////////////////////////////////////////////////////////////////
// News Response Models
///////////////////////////////////////////////////////////////////////////

class NewsListResponseModel {
    var errors: Boolean = false
    var news: List<NewsModel>? = null
}

class NewsResponseModel {
    var errors: Boolean = false
    var news: NewsModel? = null
}

class NewsModel : ViewType {
    var id: Long = -1
    var title: String? = null
    var text: String? = null
    var photo: String? = null
    var created_at: String? = null
    var updated_at: String? = null

    override fun getViewType() = AdapterConstants.NEWS
}

///////////////////////////////////////////////////////////////////////////
// Objects Response Model
///////////////////////////////////////////////////////////////////////////

class ObjectsResponseModel{
    var errors: Boolean = false
    var objects: List<ObjectModel>? = null
}

class ObjectResponseModel{
    var errors: Boolean = false
    var objects: ObjectModel? = null
}

class ObjectModel : ViewType {
    var id : Long = -1
    var title : String? = null
    var description : String? = null
    var photo : String? = null
    var created_at : String? = null
    var updated_at : String? = null
    var object_type : String? = null
    var lat: Float = 0f
    var lng: Float = 0f
    var integer : String? = null
    var thumbnail : String? = null
    var capacity : Int = 0
    var now : String? = null

    override fun getViewType() = AdapterConstants.OBJECTS
}

///////////////////////////////////////////////////////////////////////////
// Results Response Model
///////////////////////////////////////////////////////////////////////////

class EventsResponseModel{
    var errors: Boolean = false
    var events: List<EventModel>? = null
}

class EventModel : IdNameModel() {
    var contests: List<ContestsModel>? = ArrayList()
}

class ContestsModel : IdNameModel()

class ResultResponseModel: IdNameModel(){
    var results: List<ResultModel>? = ArrayList()
}

class ResultModel: ViewType{
    var place: Int = 0
    var country: String? = null
    var name: String? = null
    var points: String? = null

    override fun getViewType() = AdapterConstants.RESULT
}

abstract class IdNameModel{
    var id : Long = -1
    var name : String? = null
}