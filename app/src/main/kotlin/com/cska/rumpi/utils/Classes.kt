package com.cska.rumpi.utils

import android.support.v4.app.Fragment

inline fun <reified T : Fragment> fragmentTag(): String = T::class.java.name

inline fun <reified T : Any> className(): String = T::class.java.name
inline fun <reified T : Any> classSimpleName(): String = T::class.java.simpleName
