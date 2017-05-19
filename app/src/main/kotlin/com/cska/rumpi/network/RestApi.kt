package com.cska.rumpi.network

import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.network.models.NewsListResponseModel
import com.cska.rumpi.network.models.NewsResponseModel
import com.cska.rumpi.network.models.ObjectResponseModel
import com.cska.rumpi.network.models.ObjectsResponseModel
import com.cska.rumpi.network.models.WhetherResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers
import java.util.ArrayList


@Suppress("UNUSED_EXPRESSION")
class RestApi {
    private val TAG = RestApi::class.java.simpleName
    private val endpoints: Endpoints

    init {
        val rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())

        val gson = GsonBuilder().create()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://95.213.237.126:7777/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .build()

        endpoints = retrofit.create(Endpoints::class.java)
    }

    fun getNewsList(pivot: Long, lang: String): Observable<NewsListResponseModel?> =
            endpoints.getNewsList(1, pivot, lang).applyDefaultNetSchedulers()

    fun getNews(id: Long, lang: String): Observable<NewsResponseModel?> =
            endpoints.getNews(id, lang).applyDefaultNetSchedulers()

    fun getObjects(lang: String): Observable<ObjectsResponseModel?> =
            endpoints.getObjects(lang).applyDefaultNetSchedulers()

    fun getObject(id: Long, lang: String): Observable<ObjectResponseModel?> =
            endpoints.getObject(id, lang).applyDefaultNetSchedulers()

    fun getWeather(): Observable<WhetherResponse?> =
            endpoints.getWeather("ru").applyDefaultNetSchedulers()

    fun sendToken(id: String, token: String, lang: String) =
            endpoints.registerPushToken(deviceId = id, pushToken = token, lang = lang).applyDefaultNetSchedulers()

    fun getAllObjects() =
        Observable.zip(
                endpoints.getObjects("ru"),
                endpoints.getObjects("en")
        ) { ruData, enData -> ruData to enData }
                .map { pair ->
                    val (ruData, enData) = pair
                    DictionaryManager.storeObjects("ru", ruData?.objects ?: ArrayList())
                    DictionaryManager.storeObjects("en", enData?.objects ?: ArrayList())
                }
                .map { Unit }!!


    fun getSchedule() =
            Observable.zip(
                    endpoints.getSchedule("ru"),
                    endpoints.getSchedule("en")
            ) { ruData, enData -> ruData to enData }
                    .map { pair ->
                        val (ruData, enData) = pair
                        DictionaryManager.storeSchedule("ru", ruData ?: ArrayList())
                        DictionaryManager.storeSchedule("en", enData ?: ArrayList())
                    }
                    .map { Unit }!!

    fun getEvents() =
            Observable.zip(
                    endpoints.getEvents("ru"),
                    endpoints.getEvents("en")
            ) { ruData, enData -> ruData to enData }
                    .map { pair ->
                        val (ruData, enData) = pair
                        DictionaryManager.storeEvents("ru", ruData?.events ?: ArrayList())
                        DictionaryManager.storeEvents("en", enData?.events ?: ArrayList())
                    }
                    .map { Unit }!!

    fun getDictionary() =
            Observable.zip(
                    getEvents(),
                    getSchedule(),
                    getAllObjects()
            ) { s1, s2, s3 ->  Unit}
                    .map { Unit }.applyDefaultNetSchedulers()

    fun getResults(id: Long, lang: String) =
            endpoints.getResults(id, lang).applyDefaultNetSchedulers()

    fun getScheduleResults(id: Long, lang: String) =
            endpoints.getScheduleContest(id, lang).applyDefaultNetSchedulers()

    fun saveMedals() = endpoints.downloadFileWithFixedUrl()
}

///////////////////////////////////////////////////////////////////////////
// Apply Default Net Schedulers
///////////////////////////////////////////////////////////////////////////

fun <T> Single<T>.applyDefaultNetSchedulers(): Single<T> = this
        .observeOn(mainThread())
        .subscribeOn(Schedulers.io())

fun <T> Observable<T>.applyDefaultNetSchedulers(): Observable<T> = this
        .observeOn(mainThread())
        .subscribeOn(Schedulers.io())
