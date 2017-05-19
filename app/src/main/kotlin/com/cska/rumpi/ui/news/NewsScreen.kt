package com.cska.rumpi.ui.news

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.displayImage
import com.cska.rumpi.utils.getLocalHumanReadableAgoDateString
import com.cska.rumpi.utils.setElevationCompat
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import java.util.Date

/**
 * Created by rumpi on 05.02.2017.
 */

class NewsActivity : BaseActivity(){
    companion object {
        public const val EXTRA_NEWS_ID = "news_id_extra"

        fun launch(activity: Activity, newsId: Long) {
            val intent = activity.intentFor<NewsActivity>(EXTRA_NEWS_ID to newsId)
            activity.startActivity(intent)
        }
    }

    private val image by bindView<ImageView>(R.id.an_iv_image)
    private val shadow by bindView<View>(R.id.an_v_shadow)
    private val title by bindView<TextView>(R.id.an_tv_title)
    private val time by bindView<TextView>(R.id.an_tv_time)
    private val text by bindView<TextView>(R.id.an_tv_text)

    private val appBarLayout by bindView<AppBarLayout>(R.id.container_toolbar)

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBarLayout.setElevationCompat(0F)
        supportActionBar?.elevation = 0F

        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        val newsId = intent.getLongExtra(EXTRA_NEWS_ID, -1L)
        getNews(newsId, language)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }


    ///////////////////////////////////////////////////////////////////////////
    // get info
    ///////////////////////////////////////////////////////////////////////////

    private fun getNews(id: Long, language: String){
        RestApi().getNews(id, language)
                .doOnNext { result ->
                    val news = result?.news
                    if(news != null) {
                        if (news.photo.isNullOrEmpty()) {
                            shadow.visibility = View.GONE
                            image.visibility = View.GONE
                        }
                        else {
                            shadow.visibility = View.VISIBLE
                            image.visibility = View.VISIBLE
                            image.displayImage(news.photo)
                        }
                        title.text = news.title
                        time.text = Date().getLocalHumanReadableAgoDateString(resources, language, news.updated_at)
                        text.text = news.text
                    }

                }.map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }

}