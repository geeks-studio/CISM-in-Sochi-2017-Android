package com.cska.rumpi.ui.objects

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView

/**
 * Created by rumpi on 01.02.2017.
 */

class ObjectsFragment: BaseFragment(),
                       SwipeRefreshLayout.OnRefreshListener,
                       OnViewTypeClickListener {
    override val titleResId: Int get() = R.string.label_objects
    override val layoutResId: Int get() = R.layout.fragment_list

    private val objectsView by bindView<RecyclerView>(android.R.id.list)
    private val refresh by bindView<SwipeRefreshLayout>(R.id.container_refresh)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectsView.setHasFixedSize(true)
        objectsView.layoutManager = LinearLayoutManager(context)

        initAdapter()
        refresh.isEnabled =false

        updateObjects()
    }

    private fun initAdapter(){
        if(objectsView.adapter == null){
            objectsView.adapter = ObjectsAdapter()
            (objectsView.adapter as ObjectsAdapter).setOnClickItemListener(this)
        }
    }

    private fun updateObjects(){
        val sharedPref = activity.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        (objectsView.adapter as ObjectsAdapter).clearAndAdd(DictionaryManager.getObjects(language))
    }

    override fun onRefresh() {
        updateObjects()
    }

    override fun onItemClick(id: Long) {
        ObjectActivity.launch(activity, id)
    }
}