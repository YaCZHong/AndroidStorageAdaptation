package me.xh.androidstorageadaptation.utils

import androidx.annotation.StringRes
import me.xh.androidstorageadaptation.App

fun string(@StringRes resId: Int): String {
    return App.sInstance.getString(resId)
}
