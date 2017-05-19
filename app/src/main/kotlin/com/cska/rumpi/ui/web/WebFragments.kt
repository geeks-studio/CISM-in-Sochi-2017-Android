package com.cska.rumpi.ui.web

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.cska.rumpi.R
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.utils.bindView

/**
 * Created by rumpi on 05.02.2017.
 */

class QuizFragment : BaseFragment(){
    override val layoutResId: Int get() = R.layout.fragment_web
    override val titleResId: Int get() = R.string.label_quiz

    private val webView by bindView<WebView>(R.id.fw_web_view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadUrl("http://cismsochi2017.ru/accreditation/")
    }
}

class ChatFragment : BaseFragment(){
    override val layoutResId: Int get() = R.layout.fragment_web
    override val titleResId: Int get() = R.string.label_chat

    private val webView by bindView<WebView>(R.id.fw_web_view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadUrl("http://cismsochi2017.ru/accreditation/")
    }
}

class ArmyFragment : BaseFragment(){
    override val layoutResId: Int get() = R.layout.fragment_web
    override val titleResId: Int get() = R.string.label_army

    private val webView by bindView<WebView>(R.id.fw_web_view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadUrl("http://cismsochi2017.ru/accreditation/")
    }
}



class MatchFragment : BaseFragment(){
    override val layoutResId: Int get() = R.layout.fragment_web
    override val titleResId: Int get() = R.string.label_video

    private val webView by bindView<WebView>(R.id.fw_web_view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadUrl("https://matchtv.ru/on-air")
    }
}