package com.applications.cleaner

import android.app.Application
import android.content.Context

class ApplicationGlobal : Application() {
    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = this
    }

    companion object {
        var gLat = 0.0
        var gLng = 0.0
        var isChatScreenOpen = false
        var applicationContext: Context? = null
    }
}
