package com.cska.rumpi.ui.results

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.network.models.IdNameModel
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.inflate
import org.jetbrains.anko.bundleOf
import stdlib.kotlin.ext.intArgument
import stdlib.kotlin.ext.longArgument

/**
 * Created by rumpi on 14.02.2017.
 */

///////////////////////////////////////////////////////////////////////////
// Events Dialog
///////////////////////////////////////////////////////////////////////////

const val EXTRA_RESULT_ID = "RESULT_ID_EXTRA"
const val EXTRA_TITLE_ID = "TITLE_ID_EXTRA"
const val EXTRA_EVENT_ID = "EVENT_ID_EXTRA"

class EventsDialog : DialogFragment() {
    companion object {
        fun create(notificationId: Int, titleId: Int) = EventsDialog().apply {
            arguments = bundleOf(
                    EXTRA_RESULT_ID to notificationId,
                    EXTRA_TITLE_ID to titleId)
        }
    }

    private val OUT_STATE_SELECTED_INDEX = "SELECTED_INDEX_OUT_STATE"

    private val resultId by intArgument(EXTRA_RESULT_ID, -1)
    private val titleId by intArgument(EXTRA_TITLE_ID, -1)
    private var selectedIndex = -1
    private var adapter : IdNameAdapter? = null

    private val clickListener = DialogInterface.OnClickListener { dialog, index ->
        selectedIndex = index
        ((parentFragment ?: activity) as IdNameListChooserDialogCallbacks)
                .onModelSelectChanged(resultId, adapter?.getItem(selectedIndex))
        getDialog().dismiss()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        selectedIndex = savedInstanceState?.getInt(OUT_STATE_SELECTED_INDEX) ?: 0
        adapter = IdNameAdapter(context, DictionaryManager.getEvents(language))
        return AlertDialog.Builder(activity)
                .setTitle(titleId)
                .setAdapter(adapter, clickListener)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(OUT_STATE_SELECTED_INDEX, selectedIndex)
    }
}


///////////////////////////////////////////////////////////////////////////
// Contest Dialog
///////////////////////////////////////////////////////////////////////////

class ContestDialog : DialogFragment() {
    companion object {
        fun create(notificationId: Int, titleId: Int, eventId: Long) = ContestDialog().apply {
            arguments = bundleOf(
                    EXTRA_RESULT_ID to notificationId,
                    EXTRA_TITLE_ID to titleId,
                    EXTRA_EVENT_ID to eventId)
        }
    }

    private val OUT_STATE_SELECTED_INDEX = "SELECTED_INDEX_OUT_STATE"

    private val resultId by intArgument(EXTRA_RESULT_ID, -1)
    private val titleId by intArgument(EXTRA_TITLE_ID, -1)
    private val eventId by longArgument(EXTRA_EVENT_ID, -1L)
    private var selectedIndex = -1
    private var adapter : IdNameAdapter? = null

    private val clickListener = DialogInterface.OnClickListener { dialog, index ->
        selectedIndex = index
        ((parentFragment ?: activity) as IdNameListChooserDialogCallbacks)
                .onModelSelectChanged(resultId, adapter?.getItem(selectedIndex))
        getDialog().dismiss()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        selectedIndex = savedInstanceState?.getInt(OUT_STATE_SELECTED_INDEX) ?: 0
        adapter = IdNameAdapter(context, DictionaryManager.getContests(language, eventId))
        return AlertDialog.Builder(activity)
                .setTitle(titleId)
                .setAdapter(adapter, clickListener)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(OUT_STATE_SELECTED_INDEX, selectedIndex)
    }
}

///////////////////////////////////////////////////////////////////////////
// Dialog Callbacks
///////////////////////////////////////////////////////////////////////////

interface IdNameListChooserDialogCallbacks {
    fun onModelSelectChanged(resultId: Int, model: IdNameModel?)
}

///////////////////////////////////////////////////////////////////////////
// Id Name Adapter
///////////////////////////////////////////////////////////////////////////

class IdNameAdapter(context: Context, items : List<IdNameModel>): ArrayAdapter<IdNameModel>(context, R.layout.item_spinner_list, items) {

    fun getCustomView(position: Int,
                      parent: ViewGroup): View {
        val view = parent.inflate(R.layout.item_spinner_list)
        (view.findViewById(R.id.name) as TextView).text = getItem(position).name
        return view
    }


    // It gets a View that displays the data at the specified position
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

}