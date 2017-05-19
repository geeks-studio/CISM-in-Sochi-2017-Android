package com.cska.rumpi.utils

import android.content.Context
import android.net.Uri
import com.cska.rumpi.utils.connectivityManager

///////////////////////////////////////////////////////////////////////////
// Http Status
///////////////////////////////////////////////////////////////////////////

object HttpStatus {

    const val CONTINUE: Int = 100
    const val SWITCHING_PROTOCOLS: Int = 101
    const val PROCESSING: Int = 102

    const val OK: Int = 200
    const val CREATED: Int = 201
    const val ACCEPTED: Int = 202
    const val NON_AUTHORITATIVE_INFORMATION: Int = 203
    const val NO_CONTENT: Int = 204
    const val RESET_CONTENT: Int = 205
    const val PARTIAL_CONTENT: Int = 206
    const val MULTI_STATUS: Int = 207

    const val MULTIPLE_CHOICES: Int = 300
    const val MOVED_PERMANENTLY: Int = 301
    const val MOVED_TEMPORARILY: Int = 302
    const val SEE_OTHER: Int = 303
    const val NOT_MODIFIED: Int = 304
    const val USE_PROXY: Int = 305
    const val TEMPORARY_REDIRECT: Int = 307

    const val BAD_REQUEST: Int = 400
    const val UNAUTHORIZED: Int = 401
    const val PAYMENT_REQUIRED: Int = 402
    const val FORBIDDEN: Int = 403
    const val NOT_FOUND: Int = 404
    const val METHOD_NOT_ALLOWED: Int = 405
    const val NOT_ACCEPTABLE: Int = 406
    const val PROXY_AUTHENTICATION_REQUIRED: Int = 407
    const val REQUEST_TIMEOUT: Int = 408
    const val CONFLICT: Int = 409
    const val GONE: Int = 410
    const val LENGTH_REQUIRED: Int = 411
    const val PRECONDITION_FAILED: Int = 412
    const val REQUEST_TOO_LONG: Int = 413
    const val REQUEST_URI_TOO_LONG: Int = 414
    const val UNSUPPORTED_MEDIA_TYPE: Int = 415
    const val REQUESTED_RANGE_NOT_SATISFIABLE: Int = 416
    const val EXPECTATION_FAILED: Int = 417
    const val I_AM_A_TEAPOT: Int = 418
    const val INSUFFICIENT_SPACE_ON_RESOURCE: Int = 419
    const val METHOD_FAILURE: Int = 420
    const val UNPROCESSABLE_ENTITY: Int = 422
    const val LOCKED: Int = 423
    const val FAILED_DEPENDENCY: Int = 424

    const val INTERNAL_SERVER_ERROR: Int = 500
    const val NOT_IMPLEMENTED: Int = 501
    const val BAD_GATEWAY: Int = 502
    const val SERVICE_UNAVAILABLE: Int = 503
    const val GATEWAY_TIMEOUT: Int = 504
    const val HTTP_VERSION_NOT_SUPPORTED: Int = 505
    const val INSUFFICIENT_STORAGE: Int = 507
}

fun isNetworkAvailable(context: Context?): Boolean = context?.connectivityManager?.activeNetworkInfo?.isConnected ?: false

///////////////////////////////////////////////////////////////////////////
// Uri
///////////////////////////////////////////////////////////////////////////

fun Uri.toDecodedString(): String = Uri.decode(this.toString())
fun Uri.toFilePathString(): String = Uri.decode(this.path)
