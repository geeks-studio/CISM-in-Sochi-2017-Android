package com.cska.rumpi.ui.objects

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.utils.bindView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor




/**
 * Created by rumpi on 08.02.2017.
 */
class MapsActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    companion object {
        const val EXTRA_LAT = "lat_extra"
        const val EXTRA_LNG = "lng_extra"
        const val EXTRA_NAME = "name_extra"

        fun launch(activity: Activity, lat: Float, lng: Float, name: String) {
            val intent = activity.intentFor<MapsActivity>(
                    EXTRA_LAT to lat,
                    EXTRA_LNG to lng,
                    EXTRA_NAME to name)
            activity.startActivity(intent)
        }
    }

    private val plusBtn by bindView<View>(R.id.am_btn_plus)
    private val minusBtn by bindView<View>(R.id.am_btn_minus)
    private val googleBtn by bindView<View>(R.id.am_go_google)
    private val yandexBtn by bindView<View>(R.id.am_go_yandex)


    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.title_activity_maps)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        plusBtn.setOnClickListener(this)
        minusBtn.setOnClickListener(this)
        googleBtn.setOnClickListener(this)
        yandexBtn.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isMapToolbarEnabled = false
        // Add a marker in Sydney and move the camera
        val lat = intent.getFloatExtra(EXTRA_LAT, 0F)
        val lng = intent.getFloatExtra(EXTRA_LNG, 0F)
        val name = intent.getStringExtra(EXTRA_NAME)
        val sydney = LatLng(lat.toDouble(), lng.toDouble())
        mMap!!.addMarker(MarkerOptions().position(sydney).title(name))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16F))
    }

    override fun onClick(v: View) {
        val lat = intent.getFloatExtra(EXTRA_LAT, 0F)
        val lng = intent.getFloatExtra(EXTRA_LNG, 0F)
        val label = intent.getStringExtra(EXTRA_NAME)
        when(v.id){
            R.id.am_btn_minus -> mMap?.animateCamera(CameraUpdateFactory.zoomOut())
            R.id.am_btn_plus -> mMap?.animateCamera(CameraUpdateFactory.zoomIn())
            R.id.am_go_google -> {
                val gmmIntentUri = Uri.parse("geo:$lat,$lng")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent)
                }
            }
            R.id.am_go_yandex -> {
                val uri = Uri.parse("yandexmaps://maps.yandex.ru/?ll=$lat,$lng&z=14")
                var intent = Intent(Intent.ACTION_VIEW, uri)
                val packageManager = packageManager
                val activities = packageManager.queryIntentActivities(intent, 0)
                val isIntentSafe = activities.size > 0
                if (isIntentSafe) {
                    startActivity(intent)
                }
                else {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=ru.yandex.yandexmaps")
                    startActivity(intent)
                }
            }
        }
    }
}