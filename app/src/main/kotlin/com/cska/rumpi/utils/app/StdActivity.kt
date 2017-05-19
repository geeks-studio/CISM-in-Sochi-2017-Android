package com.cska.rumpi.utils.app

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import android.widget.Toast
import com.cska.rumpi.R
import com.cska.rumpi.utils.Loggable

///////////////////////////////////////////////////////////////////////////
// Std Activity
///////////////////////////////////////////////////////////////////////////

abstract class StdActivity() : AppCompatActivity(), Loggable, Toaster {

    private val TAG_PROGRESS_DIALOG = "std_activity::progress_dialog_tag"

    private var progressDialog: ProgressDialog? = null

    private var errorToast: Toast? = null

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        progressDialog = findFragmentByTag(TAG_PROGRESS_DIALOG) as? ProgressDialog
        progressDialog?.isCancelable = false
    }

    override fun onStop() {
        errorToast?.cancel()
        errorToast = null
        super.onStop()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Fragment helpers
    ///////////////////////////////////////////////////////////////////////////

    protected fun findFragmentByTag(fragmentTag: String): Fragment? =
            supportFragmentManager.findFragmentByTag(fragmentTag)

    protected fun findFragmentById(@IdRes fragmentId: Int): Fragment? =
            supportFragmentManager.findFragmentById(fragmentId)

    open protected fun placeFragment(fragmentTag: String, args: Bundle? = null): Fragment? {
        if (isFinishing)
            return null

        val transaction = supportFragmentManager.beginTransaction()

        val fragment = Fragment.instantiate(this, fragmentTag, args)
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


    ///////////////////////////////////////////////////////////////////////////
    // Toaster impl
    ///////////////////////////////////////////////////////////////////////////

    private fun showToast(@StringRes messageResId: Int) = showToast(getText(messageResId))
    private fun showToast(message: CharSequence) = makeToast(message).let { toast ->
        errorToast?.cancel()
        errorToast = null
        toast
    }.show()


    override fun showToast(@StringRes messageResId: Int, message: CharSequence?) {
        if (messageResId > 0)
            showToast(messageResId)
        else if (message != null)
            showToast(message)
    }

    protected fun makeToast(message: CharSequence, isError: Boolean = false): Toast =
            Toast.makeText(this, message, if (!isError) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)


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

    val isProgressDialogVisible: Boolean
        get() = findFragmentByTag(TAG_PROGRESS_DIALOG) != null

    fun showProgressDialog(@StringRes messageResId: Int) =
            showProgressDialog(getText(messageResId))

    fun showProgressDialog(message: CharSequence) {

        try {
            if (!isFinishing && progressDialog == null) {
                val args = Bundle()
                args.putCharSequence("message", message)
                progressDialog = ProgressDialog()
                progressDialog!!.arguments = args
                progressDialog!!.isCancelable = true
                progressDialog!!.show(supportFragmentManager, TAG_PROGRESS_DIALOG)
            }
        }
        catch(ex: IllegalStateException) {
            // oops...
        }
    }

    fun hideProgressDialog() {
        try {
            if (!isFinishing) {
                if (progressDialog == null)
                    progressDialog = findFragmentByTag(TAG_PROGRESS_DIALOG) as? ProgressDialog?

                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                }
            }
        }
        catch(ignored: IllegalStateException) {
        }
    }
}


///////////////////////////////////////////////////////////////////////////
// Progress Dialog
///////////////////////////////////////////////////////////////////////////

class ProgressDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = android.app.ProgressDialog(activity)
        dialog.setMessage(arguments.getCharSequence("message"))
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}
