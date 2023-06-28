package me.xh.fileprovider

import android.app.Application
import me.xh.fileprovider.utils.AssetsUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        AssetsUtils.copyFile()
    }

    companion object {
        lateinit var sInstance: App
    }
}