package me.xh.fileprovider.utils

import androidx.annotation.StringRes
import me.xh.fileprovider.App

fun string(@StringRes resId: Int): String {
    return App.sInstance.getString(resId)
}
