package com.cska.rumpi.ui.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.cska.rumpi.R
import com.cska.rumpi.utils.app.StdActivity
import io.fabric.sdk.android.Fabric
import org.jetbrains.anko.find

///////////////////////////////////////////////////////////////////////////
// Base Activity
///////////////////////////////////////////////////////////////////////////

open class BaseActivity : StdActivity() {
    override fun onStart() {
        super.onStart()
        Fabric.with(this, Crashlytics())
    }
}

///////////////////////////////////////////////////////////////////////////
// Single Fragment Activity
///////////////////////////////////////////////////////////////////////////

abstract class SingleFragmentActivity : BaseActivity() {

    abstract protected val fragmentTag: String

    open protected val layoutResId: Int
        get() = R.layout.activity_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        if (savedInstanceState == null)
            placeFragment(fragmentTag, intent.extras)
    }
}


///////////////////////////////////////////////////////////////////////////
// Single Fragment Activity
///////////////////////////////////////////////////////////////////////////

abstract class SingleFragmentToolbarActivity : BaseActivity() {

    abstract protected val fragmentTag: String

    open protected val layoutId: Int
        get() = R.layout.activity_fragment_container_with_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            placeFragment(fragmentTag, intent.extras)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }
}


///////////////////////////////////////////////////////////////////////////
// Full Screen Dialog Activity
///////////////////////////////////////////////////////////////////////////

abstract class FullScreenDialogActivity : SingleFragmentToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            setResult(RESULT_CANCELED)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
