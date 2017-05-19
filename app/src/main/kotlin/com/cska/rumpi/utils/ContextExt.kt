package com.cska.rumpi.utils

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.PowerManager
import android.os.Vibrator
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.configuration

private const val TABLET_SMALLEST_WIDTH = 600

///////////////////////////////////////////////////////////////////////////
// Hardware
///////////////////////////////////////////////////////////////////////////

val Context.hasSoftwareNavBar: Boolean
    get() = hasSoftwareNavBar(this)

val Context.statusBarHeight: Int
    get() = getStatusBarHeight(this)

val Context.actionBarHeight: Int
    get() = getActionBarHeight(this)

val Context.navBarHeight: Int
    get() = getNavigationBarHeight(this)

val Context.isPhone: Boolean
    get() = configuration.isPhone

val Configuration.isPhone: Boolean
    get() = smallestScreenWidthDp < TABLET_SMALLEST_WIDTH

val Context.isTablet: Boolean
    get() = configuration.isTablet

val Configuration.isTablet: Boolean
    get() = smallestScreenWidthDp >= TABLET_SMALLEST_WIDTH

val Context.isPortrait: Boolean
    get() = configuration.isPortrait

val Configuration.isPortrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT

val Context.isLandscape: Boolean
    get() = configuration.isLandscape

val Configuration.isLandscape: Boolean
    get() = orientation == Configuration.ORIENTATION_LANDSCAPE

///////////////////////////////////////////////////////////////////////////
// Services
///////////////////////////////////////////////////////////////////////////

val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

val Context.notificationManager: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

val Context.vibrator: Vibrator
    get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

val Context.downloadManager: DownloadManager
    get() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

val Context.powerManager: PowerManager
    get() = getSystemService(Context.POWER_SERVICE) as PowerManager

val Context.keyguardManager: KeyguardManager
    get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

///////////////////////////////////////////////////////////////////////////
// Intents
///////////////////////////////////////////////////////////////////////////

fun Context.browse(uri: Uri): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        startActivity(intent)
        return true
    }
    catch (e: ActivityNotFoundException) {
        loge("Context::Browse", thr = e)
        return false
    }
}

fun Context.dialNumber(number: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    }
    catch (e: Exception) {
        loge("Context::DialNumber", thr = e)
        return false
    }
}

///////////////////////////////////////////////////////////////////////////
// Support
///////////////////////////////////////////////////////////////////////////

@ColorInt
fun Context.getColorCompat(@ColorRes resId: Int): Int =
        ContextCompat.getColor(this, resId)

fun Context.getColorStateListCompat(@ColorRes resId: Int): ColorStateList =
        ContextCompat.getColorStateList(this, resId)

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    try {
        return ContextCompat.getDrawable(this, resId)
    }catch (e: Exception){
        return null
    }
}

fun Context.getDimensionCompat(@DimenRes resId: Int): Float =
        resources.getDimension(resId)

fun Context.getDrawableByName(drawableName: String): Drawable? =
        getDrawableCompat(
                resources.getIdentifier(drawableName, "drawable", packageName)
        )

fun Context.getStringByName(stringName: String): String =
        getString(
                resources.getIdentifier(stringName, "string", packageName)
        )

/**
 * Retrieve a dimensional for a particular resource ID for use
 * as a size in raw pixels.  This is the same as
 * {@link #getDimension}, except the returned value is converted to
 * integer pixels for use as a size.  A size conversion involves
 * rounding the base value, and ensuring that a non-zero base value
 * is at least one pixel in size.
 *
 * @param id The desired resource identifier, as generated by the aapt
 *           tool. This integer encodes the package, type, and resource
 *           entry. The value 0 is an invalid identifier.
 *
 * @return Resource dimension value multiplied by the appropriate
 * metric and truncated to integer pixels.
 *
 * @throws NotFoundException Throws NotFoundException if the given ID does not exist.
 *
 * @see #getDimension
 * @see #getDimensionPixelOffset
 */
fun Context.getDimensionPixelSizeCompat(@DimenRes resId: Int): Int =
        resources.getDimensionPixelSize(resId)

/**
 * Retrieve a dimensional for a particular resource ID for use
 * as an offset in raw pixels.  This is the same as
 * {@link #getDimension}, except the returned value is converted to
 * integer pixels for you.  An offset conversion involves simply
 * truncating the base value to an integer.
 *
 * @param id The desired resource identifier, as generated by the aapt
 *           tool. This integer encodes the package, type, and resource
 *           entry. The value 0 is an invalid identifier.
 *
 * @return Resource dimension value multiplied by the appropriate
 * metric and truncated to integer pixels.
 *
 * @throws NotFoundException Throws NotFoundException if the given ID does not exist.
 *
 * @see #getDimension
 * @see #getDimensionPixelSize
 */
fun Context.getDimensionPixelOffsetCompat(@DimenRes resId: Int): Int =
        resources.getDimensionPixelOffset(resId)
