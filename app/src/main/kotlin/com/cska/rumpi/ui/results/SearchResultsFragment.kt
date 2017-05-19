package com.cska.rumpi.ui.results

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.models.IdNameModel
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.ui.pdf.CountriesActivity
import com.cska.rumpi.utils.bindView

/**
 * Created by rumpi on 01.02.2017.
 */

class SearchResultsFragment : BaseFragment(), View.OnClickListener, IdNameListChooserDialogCallbacks {
    override val titleResId: Int get() = R.string.label_results
    override val layoutResId: Int get() = R.layout.fragment_result_search

    private val DIALOG_EVENT_TYPE = 1
    private val DIALOG_CONTEST_TYPE = 2

    private val event by bindView<TextView>(R.id.frs_tv_event)
    private val contest by bindView<TextView>(R.id.frs_tv_contest)

    private val search by bindView<FrameLayout>(R.id.frs_btn_search)
    private val eventsBtn by bindView<FrameLayout>(R.id.event_container)
    private val contestsBtn by bindView<FrameLayout>(R.id.contest_container)
    private val countries by bindView<View>(R.id.participating_countries)

    private var selectedEvent : IdNameModel? = null
    private var selectedContest : IdNameModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search.setOnClickListener(this)
        eventsBtn.setOnClickListener(this)
        contestsBtn.setOnClickListener(this)
        countries.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.frs_btn_search -> {
                if((selectedEvent?.id ?: -1L) == -1L
                    ||(selectedContest?.id ?: -1L) == -1L){
                    showToast(R.string.label_select_event_and_content)
                }else {
                    ResultActivity.launch(
                            activity,
                            selectedContest?.id ?: -1,
                            selectedEvent?.name ?: "",
                            selectedContest?.name ?: "")
                }
            }
            R.id.contest_container ->{
                if((selectedEvent?.id ?: -1L) == -1L){
                    showToast(R.string.label_select_event)
                }else {
                    ContestDialog
                            .create(DIALOG_CONTEST_TYPE, R.string.label_contest, selectedEvent?.id ?: -1L)
                            .show(childFragmentManager, "contest_chooser_dialog_tag")
                }
            }
            R.id.event_container ->{
                EventsDialog
                        .create(DIALOG_EVENT_TYPE, R.string.label_event)
                        .show(childFragmentManager, "event_chooser_dialog_tag")
            }
            R.id.participating_countries -> CountriesActivity.launch(activity)
        }

    }

    override fun onModelSelectChanged(resultId: Int, model: IdNameModel?) {
        when(resultId){
            DIALOG_CONTEST_TYPE -> {
                selectedContest = model
                contest.text = model?.name
            }
            DIALOG_EVENT_TYPE -> {
                selectedEvent = model
                event.text = model?.name
                selectedContest = null
                contest.text = ""
            }
        }
    }


}




