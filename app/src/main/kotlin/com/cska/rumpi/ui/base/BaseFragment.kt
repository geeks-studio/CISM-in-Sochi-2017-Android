package com.cska.rumpi.ui.base

import android.support.v7.widget.Toolbar
import com.cska.rumpi.R
import com.cska.rumpi.utils.app.StdFragment
import org.jetbrains.anko.findOptional

///////////////////////////////////////////////////////////////////////////
// Base Fragment
///////////////////////////////////////////////////////////////////////////

abstract class BaseFragment : StdFragment() {

    final override val baseActivity: BaseActivity?
        get() = activity as? BaseActivity

    override fun onStart() {
        super.onStart()
        //Crashlytics.setString(CrashlyticsKeys.CURRENT_FRAGMENT, javaClass.simpleName)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    protected fun initSupportActionBar(displayHomeAsUp: Boolean = true) {
        val toolbar = view?.findOptional<Toolbar>(R.id.toolbar)
        if (toolbar == null) {
            error("initSupportActionBar($displayHomeAsUp) was called, but view contain no toolbar!")
            return
        }

        val baseActivity = baseActivity
        if (baseActivity == null) {
            error("baseActivity == null, you should call this method within onViewCreated() or onActivityCreated()")
            return
        }

        baseActivity.setSupportActionBar(toolbar)
        baseActivity.supportActionBar?.setDisplayHomeAsUpEnabled(displayHomeAsUp)
    }
}
