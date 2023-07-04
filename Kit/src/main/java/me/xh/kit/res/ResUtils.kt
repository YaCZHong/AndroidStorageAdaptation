package me.xh.kit.res

import androidx.annotation.StringRes
import me.xh.kit.app.KitConfig

fun string(@StringRes resId: Int): String {
    return KitConfig.mContext.getString(resId)
}
