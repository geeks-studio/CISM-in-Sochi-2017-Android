package com.cska.rumpi.utils.app

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.utils.Loggable

///////////////////////////////////////////////////////////////////////////
// Std Fragment
///////////////////////////////////////////////////////////////////////////

abstract class StdFragment : Fragment(),
                             Loggable,
                             Toaster {

    private val OUT_STATE_PENDING_DIALOG_HIDING = "pending_dialog_hiding_out_state"
    private var pendingDialogHiding = false

    @StringRes
    open protected val titleResId: Int = -1
    abstract protected val layoutResId: Int @LayoutRes get

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(layoutResId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (titleResId != -1)
            baseActivity?.setTitle(titleResId)

        if (savedInstanceState != null)
            pendingDialogHiding = savedInstanceState.getBoolean(OUT_STATE_PENDING_DIALOG_HIDING, false)

        if (pendingDialogHiding)
            hideProgressDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(OUT_STATE_PENDING_DIALOG_HIDING, pendingDialogHiding)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Activity access
    ///////////////////////////////////////////////////////////////////////////

    open protected val baseActivity: StdActivity?
        get() = activity as? StdActivity

    ///////////////////////////////////////////////////////////////////////////
    // Toaster impl
    ///////////////////////////////////////////////////////////////////////////

    override fun showToast(messageResId: Int, message: CharSequence?) {
        baseActivity?.showToast(messageResId, message)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Progress dialog impl
    ///////////////////////////////////////////////////////////////////////////

    open protected val dialogRefreshable = object : Refreshable {
        override var isRefreshing: Boolean
            get() = isProgressDialogVisible
            set(value) {
                if (value) showProgressDialog(R.string.label_wait_for_it)
                else hideProgressDialog()
            }
    }

    protected val isProgressDialogVisible: Boolean get() = baseActivity?.isProgressDialogVisible ?: false

    protected fun showProgressDialog(@StringRes messageResId: Int) {
        baseActivity?.showProgressDialog(messageResId)
    }

    protected fun showProgressDialog(message: String) {
        baseActivity?.showProgressDialog(message)
    }

    protected fun hideProgressDialog() {
        val activity = baseActivity
        if (activity != null && !activity.isFinishing)
            activity.hideProgressDialog()
        else
            pendingDialogHiding = true
    }

    ///////////////////////////////////////////////////////////////////////////
    // Fragment helpers
    ///////////////////////////////////////////////////////////////////////////

    protected fun findFragmentByTag(fragmentTag: String): Fragment? =
            childFragmentManager.findFragmentByTag(fragmentTag)

    protected fun findFragmentById(@IdRes fragmentId: Int): Fragment? =
            childFragmentManager.findFragmentById(fragmentId)

    open protected fun placeFragment(fragmentTag: String, args: Bundle? = null): Fragment? {
        if (activity?.isFinishing ?: true)
            return null

        val transaction = childFragmentManager.beginTransaction()

        val fragment = instantiate(context, fragmentTag, args)
        transaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_out, android.R.anim.fade_in)
        transaction.replace(R.id.container_fragments, fragment, fragmentTag)

        if (addToBackStack)
            transaction.addToBackStack(fragmentTag)

        transaction.commit()

        return fragment
    }

    open protected val addToBackStack: Boolean
        get() = !isFragmentContainerEmpty

    open protected val isFragmentContainerEmpty: Boolean
        get() {
            val prevFragment = findFragmentById(R.id.container_fragments)
            return prevFragment == null || prevFragment.isDetached
        }
}


///////////////////////////////////////////////////////////////////////////
// Stub Fragments
///////////////////////////////////////////////////////////////////////////

open class StubFragment : StdFragment() {

    open val emptyText: String? = null
    override val layoutResId: Int get() = R.layout.fragment_stub

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!emptyText.isNullOrEmpty())
            (view as TextView).text = emptyText
    }
}
