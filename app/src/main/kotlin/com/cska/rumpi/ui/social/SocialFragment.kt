package com.cska.rumpi.ui.social

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.ui.base.BaseFragment
import com.cska.rumpi.utils.bindView


/**
 * Created by rumpi on 05.02.2017.
 */

class SocialFragment: BaseFragment(), View.OnClickListener{
    override val titleResId: Int get() = R.string.label_social
    override val layoutResId: Int get() = R.layout.fragment_social

    private val facebook by bindView<View>(R.id.facebook)
    private val twitter by bindView<View>(R.id.twitter)
    private val instargram by bindView<View>(R.id.instagram)
    private val odnoklas by bindView<View>(R.id.odnoklassniki)
    private val vk by bindView<View>(R.id.vk)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facebook.setOnClickListener(this)
        twitter.setOnClickListener(this)
        instargram.setOnClickListener(this)
        odnoklas.setOnClickListener(this)
        vk.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.facebook -> openLink(FACEBOOK_APP_PACKAGE_ID)
            R.id.twitter -> {openLink(TWITTER_APP_PACKAGE_ID)}
            R.id.instagram -> {openLink(INSTARGRAM_APP_PACKAGE_ID)}
            R.id.odnoklassniki -> {openLink(ODNOKLASSNIKI_APP_PACKAGE_ID)}
            R.id.vk -> {openLink(VK_APP_PACKAGE_ID)}
        }

    }

    private val VK_APP_PACKAGE_ID = "com.vkontakte.android"
    private val FACEBOOK_APP_PACKAGE_ID = "com.facebook.katana"
    private val INSTARGRAM_APP_PACKAGE_ID = "com.instagram.android"
    private val TWITTER_APP_PACKAGE_ID = "com.twitter.android"
    private val ODNOKLASSNIKI_APP_PACKAGE_ID = "ru.ok.android"

    fun openLink(social: String) {
        when(social){
            FACEBOOK_APP_PACKAGE_ID ->{
                try {
                    val versionCode = context.packageManager.getPackageInfo(FACEBOOK_APP_PACKAGE_ID, 0)
                    if (versionCode.versionCode >= 3002850L) { //newer versions of fb app
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/cismsochi2017")))
                    }
                    else { //older versions of fb app
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/cismsochi2017")))
                    }
                }
                catch (e: Exception) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/cismsochi2017")))
                }
            }
            VK_APP_PACKAGE_ID ->{
                try {
                    context.packageManager.getPackageInfo(VK_APP_PACKAGE_ID, 0)
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vkontakte://cismsochi2017")))
                }
                catch (e: Exception) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/cismsochi2017")))
                }
            }
            INSTARGRAM_APP_PACKAGE_ID ->{
                try {
                    context.packageManager.getPackageInfo(INSTARGRAM_APP_PACKAGE_ID, 0)
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("instagram://cismsochi2017")))
                }
                catch (e: Exception) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/cismsochi2017/")))
                }
            }
            TWITTER_APP_PACKAGE_ID ->{
                try {
                    context.packageManager.getPackageInfo(TWITTER_APP_PACKAGE_ID, 0)
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("twitter://cismsochi2017")))
                }
                catch (e: Exception) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/cismsochi2017")))
                }
            }
            ODNOKLASSNIKI_APP_PACKAGE_ID ->{
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://ok.ru/cismsochi2017")))
            }
        }
    }

}