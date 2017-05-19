package com.cska.rumpi.network

import com.cska.rumpi.network.models.EventsResponseModel
import com.cska.rumpi.network.models.MainResponse
import com.cska.rumpi.network.models.NewsListResponseModel
import com.cska.rumpi.network.models.NewsResponseModel
import com.cska.rumpi.network.models.ObjectResponseModel
import com.cska.rumpi.network.models.ObjectsResponseModel
import com.cska.rumpi.network.models.ResultResponseModel
import com.cska.rumpi.network.models.ScheduleItemModel
import com.cska.rumpi.network.models.ScheduleListResponseModel
import com.cska.rumpi.network.models.WhetherResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable


///////////////////////////////////////////////////////////////////////////
//Endpoints
///////////////////////////////////////////////////////////////////////////

const val REQUEST_LIMIT = 20
const val REQUEST_LIMIT_MAX = 1000
const val LOAD_MORE_OFFSET = REQUEST_LIMIT / 2
const val REQUEST_LIMIT_CHATS = REQUEST_LIMIT * 2

interface Endpoints {

    ///////////////////////////////////////////////////////////////////////////
    // Auth
    ///////////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("api/v1/push")
    fun registerPushToken(
            @Field("deviceId") deviceId: String,
            @Field("pushToken") pushToken: String,
            @Field("appId") appId: Int = 1,
            @Field("deviceType") deviceType: String = "android",
            @Field("lang") lang: String = "ru"
    ): Observable<MainResponse?>


    ///////////////////////////////////////////////////////////////////////////
    // News stuff
    ///////////////////////////////////////////////////////////////////////////

    @GET("api/v1/news?limit=$REQUEST_LIMIT")
    fun getNewsList(
            @Query("ascending") ascending: Long,
            @Query("pivot") pivot :Long,
            @Query("lang") lang: String = "ru"
    ): Observable<NewsListResponseModel?>

    @GET("api/v1/news/{id}")
    fun getNews(
            @Path("id") id: Long,
            @Query("lang") lang: String = "ru"
    ): Observable<NewsResponseModel?>


    ///////////////////////////////////////////////////////////////////////////
    // Objects stuff
    ///////////////////////////////////////////////////////////////////////////

    @GET("api/v1/objects")
    fun getObjects(@Query("lang") lang: String = "ru"): Observable<ObjectsResponseModel?>

    @GET("api/v1/objects/{id}")
    fun getObject(@Path("id") id: Long,
                  @Query("lang") lang: String = "ru")
            : Observable<ObjectResponseModel?>


    ///////////////////////////////////////////////////////////////////////////
    // Weather stuff
    ///////////////////////////////////////////////////////////////////////////

    @GET("api/v1/weather")
    fun getWeather(@Query("lang") lang: String = "ru"): Observable<WhetherResponse?>

    ///////////////////////////////////////////////////////////////////////////
    // Schedule stuff
    ///////////////////////////////////////////////////////////////////////////

    @GET("api/v1/schedule")
    fun getSchedule(@Query("lang") lang: String = "ru"): Observable<List<ScheduleListResponseModel>?>

    @GET("api/v1/schedule/contest")
    fun getScheduleContest(@Query("id") id: Long,
                           @Query("lang") lang: String = "ru"): Observable<List<ScheduleItemModel>?>

    ///////////////////////////////////////////////////////////////////////////
    // Results stuff
    ///////////////////////////////////////////////////////////////////////////

    @GET("api/v1/events")
    fun getEvents(@Query("lang") lang: String = "ru"): Observable<EventsResponseModel?>

    @GET("api/v1/contests/{id}")
    fun getResults(@Path("id") id: Long,
                   @Query("lang") lang: String = "ru"): Observable<ResultResponseModel?>

    @GET("award/award_doc_ru.pdf")
    fun downloadFileWithFixedUrl(): Call<ResponseBody>

}
