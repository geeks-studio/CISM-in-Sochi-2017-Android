package com.cska.rumpi.utils

import android.content.res.Resources
import com.cska.rumpi.R
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


private const val FORMAT_SERVER_FULL: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private val formatterServerFull: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_SERVER_FULL, Locale.getDefault())

private const val FORMAT_SERVER_DATE: String = "yyyy-MM-dd"
private val formatterServerDate: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_SERVER_DATE, Locale.getDefault())

private const val FORMAT_UI_DATE: String = "dd.MM.yyyy"
private val formatterUiDate: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_UI_DATE, Locale.getDefault())

private const val FORMAT_DAY_DATE: String = "dd.MM"
private val formatterDayDate: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_DAY_DATE, Locale.getDefault())

private const val FORMAT_FILE_NAME: String = "yyyy-MM-dd HH-mm-ss"
private val formatterFileName: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_FILE_NAME, Locale.getDefault())

private const val FORMAT_EXPIRATION_DATE: String = "MM'/'yy"
private val formatterExpirationDate: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_EXPIRATION_DATE, Locale.getDefault())

private const val FORMAT_EVENT_DATE: String = "HH:mm"
private val formatterEventDate: SimpleDateFormat
    get() = SimpleDateFormat(FORMAT_EVENT_DATE, Locale.getDefault())

fun SimpleDateFormat.format(): String = this.format(java.util.Date())

fun Date.toServerFullString(): String = formatterServerFull.format(this)
fun Date.toServerDateString(): String = formatterServerDate.format(this)
fun Date.toUiDateString(): String = formatterUiDate.format(this)
fun Date.toDayDateString(): String = formatterDayDate.format(this)
fun Date.toEventString(): String = formatterEventDate.format(this)
fun Date.toExpirationDateString(): String = formatterExpirationDate.format(this)

fun Date.toCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}

fun Date.format(pattern: String, locale: Locale = Locale.getDefault(), timeZone: TimeZone = TimeZone.getDefault()): String =
        SimpleDateFormat(pattern, locale).apply { this.timeZone = timeZone }.format(this)


///////////////////////////////////////////////////////////////////////////
// Time helpers
///////////////////////////////////////////////////////////////////////////

fun getHumanReadableAgoDateString(res: Resources, millis: Long): String {
    val now = LocalDateTime.now()

    val instant = Instant.ofEpochMilli(millis)
    val then = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val years = then.until(now, ChronoUnit.YEARS).toInt()
    if (years > 0)
        return res.getQuantityString(R.plurals.label_ago_years, years, years)

    val months = then.until(now, ChronoUnit.MONTHS).toInt()
    if (months > 0)
        return res.getQuantityString(R.plurals.label_ago_months, months, months)

    val days = then.until(now, ChronoUnit.DAYS).toInt()
    if (days > 0)
        return res.getQuantityString(R.plurals.label_ago_days, days, days)

    val hours = then.until(now, ChronoUnit.HOURS).toInt()
    if (hours > 0)
        return res.getQuantityString(R.plurals.label_ago_hours, hours, hours)

    val minutes = then.until(now, ChronoUnit.MINUTES).toInt()
    if (minutes > 1)
        return res.getQuantityString(R.plurals.label_ago_minutes, minutes, minutes)
    else
        return res.getString(R.string.label_just_now)
}

fun Date.getEnglishFriendlyTime(): String {
    val sb = StringBuffer()
    val current = Calendar.getInstance().time
    var diffInSeconds = ((current.time - this.time) / 1000).toInt()

    val sec = if (diffInSeconds >= 60) (diffInSeconds % 60).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 60
    val min = if (diffInSeconds >= 60) (diffInSeconds % 60).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 60
    val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 24
    val days = if (diffInSeconds >= 30) (diffInSeconds % 30).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 30
    var months = if (diffInSeconds >= 12) (diffInSeconds % 12).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 12
    val years = diffInSeconds.toInt()

    if (years > 0) {
        if (years == 1) {
            sb.append("a year")
        } else {
            sb.append("$years years")
        }
        if (years <= 6 && months > 0) {
            if (months == 1) {
                sb.append(" and a month")
            } else {
                sb.append(" and $months months")
            }
        }
    } else if (months > 0) {
        if (months == 1) {
            sb.append("a month")
        } else {
            sb.append("$months months")
        }
        if (months <= 6 && days > 0) {
            if (days == 1) {
                sb.append(" and a day")
            } else {
                sb.append(" and $days days")
            }
        }
    } else if (days > 0) {
        if (days == 1) {
            sb.append("a day")
        } else {
            sb.append("$days days")
        }
        if (days <= 3 && hrs > 0) {
            if (hrs == 1) {
                sb.append(" and an hour")
            } else {
                sb.append(" and $hrs hours")
            }
        }
    } else if (hrs > 0) {
        if (hrs == 1) {
            sb.append("an hour")
        } else {
            sb.append("$hrs hours")
        }
        if (min > 1) {
            sb.append(" and $min minutes")
        }
    } else if (min > 0) {
        if (min == 1) {
            sb.append("a minute")
        } else {
            sb.append("$min minutes")
        }
        if (sec > 1) {
            sb.append(" and $sec seconds")
        }
    } else {
        if (sec <= 1) {
            sb.append("about a second")
        } else {
            sb.append("about $sec seconds")
        }
    }

    sb.append(" ago")

    return sb.toString()
}

fun Date.getLocalHumanReadableAgoDateString(res: Resources, locale: String, dateString: String?): String{
    if (dateString == null) return ""
    this.time = fromFullString(dateString).time
    when{
        locale == "ru" -> return getHumanReadableAgoDateString(res, this.time).toUpperCase()
        else -> return getEnglishFriendlyTime().toUpperCase()

    }
}




///////////////////////////////////////////////////////////////////////////
// Local Date helpers
///////////////////////////////////////////////////////////////////////////

fun Date.toLocalDate(): LocalDate = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()
fun Date.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
fun LocalDate.toDate(): Date = Date(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())

fun LocalDate.atEndOfDay(): LocalDateTime =
        this.atStartOfDay().plusDays(1).minus(1, ChronoUnit.MILLIS)

fun Date.fromFullString(dateString: String) = formatterServerFull.parse(dateString)!!