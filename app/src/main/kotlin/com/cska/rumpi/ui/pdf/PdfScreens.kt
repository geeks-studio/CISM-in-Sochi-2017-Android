package com.cska.rumpi.ui.pdf

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.github.barteksc.pdfviewer.PDFView
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import java.io.File

/**
 * Created by rumpi on 13.02.2017.
 */

abstract class PdfFragment : BaseFragment() {
    override val layoutResId: Int get() = R.layout.fragment_pdf

    protected val pdfView by bindView<PDFView>(R.id.pdfView)

    abstract protected val file: String get

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

    }

    open protected fun loadData(){
        pdfView.fromAsset(file)
                .enableAnnotationRendering(true)
                .onLoad({ page ->
                    val pageWidth = pdfView.optimalPageWidth
                    val viewWidth = pdfView.width
                    pdfView.minZoom = viewWidth / pageWidth
                    pdfView.resetZoom()
                    pdfView.moveTo(0f, 0f)
                })
                .load()
    }

}

class CismFragment : PdfFragment() {
    override val titleResId: Int get() = R.string.label_cism
    override val file: String get(){
        val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        when(language){
            "ru" -> return "CISM_ru.pdf"
            else ->return "CISM_en.pdf"
        }
    }
}

class MedalFragment : PdfFragment() {
    override val titleResId: Int get() = R.string.label_army
    override val file: String get() = "medals.pdf"

    override fun loadData(){
        val pdf = File(activity.filesDir, "medals.pdf")
        pdfView.fromFile(pdf)
                .enableAnnotationRendering(true)
                .onLoad({ page ->
                    val pageWidth = pdfView.optimalPageWidth
                    val viewWidth = pdfView.width
                    pdfView.minZoom = viewWidth / pageWidth
                    pdfView.resetZoom()
                    pdfView.moveTo(0f, 0f)
                })
                .load()
    }
}

class CountriesActivity : BaseActivity(){
    companion object {

        fun launch(activity: Activity) {
            val intent = activity.intentFor<CountriesActivity>()
            activity.startActivity(intent)
        }
    }

    private val pdfView by bindView<PDFView>(R.id.pdfView)

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.label_participating_countries)

        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "ru")
        when(language){
            "ru" -> loadRu()
            else -> loadEn()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun loadRu(){
        pdfView.fromAsset("CISM_countries_rus.pdf")
                .enableAnnotationRendering(true)
                .onLoad({ page ->
                    val pageWidth = pdfView.optimalPageWidth
                    val viewWidth = pdfView.width
                    pdfView.minZoom = viewWidth / pageWidth
                    pdfView.resetZoom()
                    pdfView.moveTo(0f, 0f)
                })
                .load()
    }

    fun loadEn(){
        pdfView.fromAsset("CISM_countries_eng.pdf")
                .enableAnnotationRendering(true)
                .onLoad({ page ->
                    val pageWidth = pdfView.optimalPageWidth
                    val viewWidth = pdfView.width
                    pdfView.minZoom = viewWidth / pageWidth
                    pdfView.resetZoom()
                    pdfView.moveTo(0f, 0f)
                })
                .load()
    }
}