package com.cska.rumpi.ui.news

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.network.REQUEST_LIMIT
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import java.util.ArrayList

/**
 * Created by rumpi on 01.02.2017.
 */

class NewsFragment: BaseFragment(),
                    SwipeRefreshLayout.OnRefreshListener,
                    OnViewTypeClickListener {
    override val titleResId: Int get() = R.string.label_news
    override val layoutResId: Int get() = R.layout.fragment_list

    private val newsView by bindView<RecyclerView>(android.R.id.list)
    private val refresh by bindView<SwipeRefreshLayout>(R.id.container_refresh)

    private var pivot = 0L
    private var isLoad = false
    private val updateListener = object : OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0){
                val visibleItemCount = newsView.layoutManager.childCount
                val totalItemCount = newsView.layoutManager.itemCount
                val pastVisibleItems = (newsView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                 if (isLoad) {
                     if (visibleItemCount + pastVisibleItems == totalItemCount && totalItemCount % REQUEST_LIMIT == 0
                         && totalItemCount > 0) {
                         isLoad = false
                         pivot  = (newsView.adapter as NewsAdapter).getLastNewsId()
                         updateNews()
                     }
                 }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsView.setHasFixedSize(true)
        newsView.layoutManager = LinearLayoutManager(context)

        initAdapter()
        refresh.setOnRefreshListener(this)

        updateNews()
    }

    private fun initAdapter(){
        if(newsView.adapter == null){
            newsView.adapter = NewsAdapter()
            (newsView.adapter as NewsAdapter).setOnClickItemListener(this)
        }
    }

    private fun updateNews(){
        val sharedPref = activity.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        newsView.removeOnScrollListener(updateListener)
        RestApi().getNewsList(pivot, language)
                .doOnNext { result ->
                    if (refresh.isRefreshing) {
                        refresh.isRefreshing = false
                    }
                    if(pivot == 0L) (newsView.adapter as NewsAdapter).clearAndAddNews(result?.news ?: ArrayList())
                    else (newsView.adapter as NewsAdapter).addNews(result?.news ?: ArrayList())
                    isLoad = true
                    newsView.addOnScrollListener(updateListener)

                }.map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }

    override fun onRefresh() {
        pivot = 0L
        updateNews()
    }

    override fun onItemClick(id: Long) {
        NewsActivity.launch(activity, id)
    }

}

