package com.cska.rumpi.ui.objects

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.network.models.ObjectModel
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.getDrawableByName
import com.cska.rumpi.utils.setElevationCompat
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor

/**
 * Created by rumpi on 05.02.2017.
 */

class ObjectActivity : BaseActivity(),
                       View.OnClickListener{
    companion object {
        const val EXTRA_OBJECT_ID = "object_id_extra"

        fun launch(activity: Activity, objectId: Long) {
            val intent = activity.intentFor<ObjectActivity>(EXTRA_OBJECT_ID to objectId)
            activity.startActivity(intent)
        }
    }

    private val image by bindView<ImageView>(R.id.ao_iv_image)
    private val title by bindView<TextView>(R.id.ao_tv_title)
    private val type by bindView<TextView>(R.id.ao_tv_type)
    private val text by bindView<TextView>(R.id.ao_tv_text)
    private val capacity by bindView<TextView>(R.id.ao_tv_capacity)
    private val mapBtn by bindView<Button>(R.id.ao_btn_map)

    private val appBarLayout by bindView<AppBarLayout>(R.id.container_toolbar)

    private var data: ObjectModel = ObjectModel()

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBarLayout.setElevationCompat(0F)
        supportActionBar?.elevation = 0F

        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        val objectId = intent.getLongExtra(EXTRA_OBJECT_ID, -1L)
        getObject(objectId, language)
        mapBtn.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }


    ///////////////////////////////////////////////////////////////////////////
    // get info
    ///////////////////////////////////////////////////////////////////////////

    private fun getObject(id: Long, language: String){
        val objects = DictionaryManager.getObjects(language)
        for(objectModel in objects){
            if(objectModel.id == id){
                data = objectModel
                setImage(objectModel.id)
                type.text = objectModel.object_type
                title.text = objectModel.title
                text.text = objectModel.description
                capacity.text = when(language){
                    "ru" -> resources.getQuantityString(R.plurals.label_ru_capacity, objectModel.capacity, objectModel.capacity)
                    else -> resources.getQuantityString(R.plurals.label_en_capacity, objectModel.capacity, objectModel.capacity)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.ao_btn_map -> MapsActivity.launch(this, data.lat, data.lng, data.title ?: "")
        }
    }

    fun setImage(id: Long){
        when(id){
            1L -> image.setImageResource(R.drawable.ic_object_1)
            2L -> image.setImageResource(R.drawable.ic_object_2)
            3L -> image.setImageResource(R.drawable.ic_object_3)
            4L -> image.setImageResource(R.drawable.ic_object_4)
            5L -> image.setImageResource(R.drawable.ic_object_5)
        }
    }

}