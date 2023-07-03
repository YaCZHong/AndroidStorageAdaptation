package me.xh.kit.app

import android.app.Application
import android.content.Context

internal object KitConfig {
    lateinit var mApplication: Application
        private set

    val mContext: Context
        get() = mApplication.applicationContext

    fun init(application: Application) {
        mApplication = application
    }
}