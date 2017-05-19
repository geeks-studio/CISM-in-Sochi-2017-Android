package com.cska.rumpi.ui.results

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.network.models.ResultModel
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.objects.ObjectActivity
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.isVisible
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import java.util.ArrayList

/**
 * Created by rumpi on 13.02.2017.
 */

class ResultActivity : BaseActivity(), OnViewTypeClickListener {
    companion object {
        const val EXTRA_CONTEST_ID = "contest_id_extra"
        const val EXTRA_CONTEST_NAME = "contest_name_extra"
        const val EXTRA_EVENT_NAME = "event_name_extra"

        fun launch(activity: Activity, contestId: Long, nameEvent: String, nameContest: String) {
            val intent = activity.intentFor<ResultActivity>(
                    EXTRA_CONTEST_ID to contestId,
                    EXTRA_CONTEST_NAME to nameContest,
                    EXTRA_EVENT_NAME to nameEvent)
            activity.startActivity(intent)
        }
    }

    private val resultsView by bindView<RecyclerView>(android.R.id.list)
    private val eventView by bindView<TextView>(R.id.ar_tv_event)
    private val contestView by bindView<TextView>(R.id.ar_tv_contest)
    private val head by bindView<View>(R.id.main_container)

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d("Dto", intent.getStringExtra(EXTRA_EVENT_NAME))

        eventView.text = intent.getStringExtra(EXTRA_EVENT_NAME)
        contestView.text = intent.getStringExtra(EXTRA_CONTEST_NAME)

        resultsView.setHasFixedSize(true)
        resultsView.layoutManager = LinearLayoutManager(this)

        initAdapter()

        getResults()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initAdapter(){
        if(resultsView.adapter == null){
            resultsView.adapter = ResultsAdapter()
            (resultsView.adapter as ResultsAdapter).setOnClickItemListener(this)
        }
    }

    private fun getResults(){
        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        RestApi().getResults(intent.getLongExtra(EXTRA_CONTEST_ID, -1), language)
                .doOnNext { result ->
                    if((result?.results ?: ArrayList<ResultModel>()).isEmpty()){
                        head.isVisible = false
                        getSchedule()
                    }else
                        head.isVisible = true
                        (resultsView.adapter as ResultsAdapter).clearAndAdd(result?.results ?: ArrayList())

                }.map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }

    private fun getSchedule(){
        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        RestApi().getScheduleResults(intent.getLongExtra(EXTRA_CONTEST_ID, -1), language)
                .doOnNext { result ->
                    (resultsView.adapter as ResultsAdapter).clearAndAdd(result ?: ArrayList())

                }.map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }

    override fun onItemClick(id: Long) {
        ObjectActivity.launch(this, id)
    }
}