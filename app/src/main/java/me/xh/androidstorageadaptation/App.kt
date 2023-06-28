package me.xh.androidstorageadaptation

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object {
        lateinit var sInstance: App
    }
}