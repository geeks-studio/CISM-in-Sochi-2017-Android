package com.cska.rumpi.utils

import android.util.Log

/**
 * Send a {@link Log#VERBOSE} log message.
 *
 * @param tag
 *         Used to identify the source of a log message.  It usually identifies the class or activity where the log
 *         call occurs.
 * @param msg
 *         The message you would like logged.
 *  @param thr
 *          Optional {@link Throwable} for stacktrace printing
 */
fun logv(tag: String, msg: String = "", thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug) Log.v(tag, msg, thr)
}

/**
 * Send a {@link Log#DEBUG} log message.
 *
 * @param tag
 *         Used to identify the source of a log message.  It usually identifies the class or activity where the log
 *         call occurs.
 * @param msg
 *         The message you would like logged.
 *  @param thr
 *          Optional {@link Throwable} for stacktrace printing
 */
fun logd(tag: String, msg: String = "", thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug) Log.d(tag, msg, thr)
}

/**
 * Send a {@link Log#INFO} log message.
 *
 * @param tag
 *         Used to identify the source of a log message.  It usually identifies the class or activity where the log
 *         call occurs.
 * @param msg
 *         The message you would like logged.
 *  @param thr
 *          Optional {@link Throwable} for stacktrace printing
 */
fun logi(tag: String, msg: String = "", thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug) Log.i(tag, msg, thr)
}

/**
 * Send a {@link Log#WARN} log message.
 *
 * @param tag
 *         Used to identify the source of a log message.  It usually identifies the class or activity where the log
 *         call occurs.
 * @param msg
 *         The message you would like logged.
 *  @param thr
 *          Optional {@link Throwable} for stacktrace printing
 */
fun logw(tag: String, msg: String = "", thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug) Log.w(tag, msg, thr)
}

/**
 * Send a {@link Log#ERROR} log message.
 *
 * @param tag
 *         Used to identify the source of a log message.  It usually identifies the class or activity where the log
 *         call occurs.
 * @param msg
 *         The message you would like logged.
 * @param thr
 *          Optional {@link Throwable} for stacktrace printing
 */
fun loge(tag: String, msg: String = "", thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug) Log.e(tag, msg, thr)
}

