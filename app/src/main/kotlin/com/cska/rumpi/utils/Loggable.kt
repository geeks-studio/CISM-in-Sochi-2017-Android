package com.cska.rumpi.utils

import android.util.Log
import com.cska.rumpi.utils.StdLibConfig

/**
 * Interface for the Logger.
 * Normally you should pass the logger tag to the [Log] methods, such as [Log.d] or [Log.e].
 * This can be inconvenient because you should store the tag somewhere or hardcode it,
 *   which is considered to be a bad practice.
 *
 * Instead of hardcoding tags, Anko provides an [Loggable] interface. You can just add the interface to
 *   any of your classes, and use any of the provided extension functions, such as
 *   [Loggable.debug] or [Loggable.error].
 *
 * The tag is the simple class name by default, but you can change it to anything you want just
 *   by overriding the [loggerTag] property.
 */
interface Loggable {
    /**
     * The logger tag used in extension functions for the [Loggable].
     * Note that the tag length should not be more than 23 symbols.
     */
    val loggerTag: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag
            else tag.substring(0, 23)
        }

    val logEnabled: Boolean
        get() = false
}

/**
 * Send a log message with the [Log.VERBOSE] severity.
 * Note that the log message will not be written if the current log level is above [Log.VERBOSE].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.v].
 */
fun Loggable.verbose(message: Any?, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled)
        log(this, message, thr, Log.VERBOSE,
                { tag, msg -> Log.v(tag, msg) },
                { tag, msg, thr -> Log.v(tag, msg, thr) })
}

/**
 * Send a log message with the [Log.DEBUG] severity.
 * Note that the log message will not be written if the current log level is above [Log.DEBUG].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.d].
 */
fun Loggable.debug(message: Any?, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled)
        log(this, message, thr, Log.DEBUG,
                { tag, msg -> Log.d(tag, msg) },
                { tag, msg, thr -> Log.d(tag, msg, thr) })
}

/**
 * Send a log message with the [Log.INFO] severity.
 * Note that the log message will not be written if the current log level is above [Log.INFO]
 *   (it is the default level).
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.i].
 */
fun Loggable.info(message: Any?, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled)
        log(this, message, thr, Log.INFO,
                { tag, msg -> Log.i(tag, msg) },
                { tag, msg, thr -> Log.i(tag, msg, thr) })
}

/**
 * Send a log message with the [Log.WARN] severity.
 * Note that the log message will not be written if the current log level is above [Log.WARN].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.w].
 */
fun Loggable.warn(message: Any?, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled)
        log(this, message, thr, Log.WARN,
                { tag, msg -> Log.w(tag, msg) },
                { tag, msg, thr -> Log.w(tag, msg, thr) })
}

/**
 * Send a log message with the [Log.ERROR] severity.
 * Note that the log message will not be written if the current log level is above [Log.ERROR].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.e].
 */
fun Loggable.error(message: Any? = null, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled)
        log(this, message, thr, Log.ERROR,
                { tag, msg -> Log.e(tag, msg) },
                { tag, msg, thr -> Log.e(tag, msg, thr) })
}

/**
 * Send a log message with the "What a Terrible Failure" severity.
 * Report an exception that should never happen.
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.wtf].
 */
fun Loggable.wtf(message: Any?, thr: Throwable? = null) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        if (thr != null) Log.wtf(loggerTag, message?.toString() ?: "null", thr)
        else Log.wtf(loggerTag, message?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.VERBOSE] severity.
 * Note that the log message will not be written if the current log level is above [Log.VERBOSE].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.v].
 */
inline fun Loggable.verbose(message: () -> Any?) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        val tag = loggerTag
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message()?.toString() ?: "null")
        }
    }
}

/**
 * Send a log message with the [Log.DEBUG] severity.
 * Note that the log message will not be written if the current log level is above [Log.DEBUG].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.d].
 */
inline fun Loggable.debug(message: () -> Any?) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        val tag = loggerTag
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message()?.toString() ?: "null")
        }
    }
}

/**
 * Send a log message with the [Log.INFO] severity.
 * Note that the log message will not be written if the current log level is above [Log.INFO].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.i].
 */
inline fun Loggable.info(message: () -> Any?) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        val tag = loggerTag
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, message()?.toString() ?: "null")
        }
    }
}

/**
 * Send a log message with the [Log.WARN] severity.
 * Note that the log message will not be written if the current log level is above [Log.WARN].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.w].
 */
inline fun Loggable.warn(message: () -> Any?) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        val tag = loggerTag
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, message()?.toString() ?: "null")
        }
    }
}

/**
 * Send a log message with the [Log.ERROR] severity.
 * Note that the log message will not be written if the current log level is above [Log.ERROR].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.e].
 */
inline fun Loggable.error(message: () -> Any?) {
    if (StdLibConfig.allowLibDebug && logEnabled) {
        val tag = loggerTag
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, message()?.toString() ?: "null")
        }
    }
}

/**
 * Return the stack trace [String] of a throwable.
 */
fun Throwable.getStackTraceString(): String = Log.getStackTraceString(this)

private inline fun log(
        logger: Loggable,
        message: Any?,
        thr: Throwable?,
        level: Int,
        f: (String, String) -> Unit,
        fThrowable: (String, String, Throwable) -> Unit) {
    val tag = logger.loggerTag
    if (Log.isLoggable(tag, level)) {
        if (thr != null) {
            fThrowable(tag, message?.toString() ?: "null", thr)
        }
        else {
            f(tag, message?.toString() ?: "null")
        }
    }
}
