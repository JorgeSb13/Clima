package com.examen.clima

import android.app.Application
import android.content.Context

val prefHelper: PreferencesHelper by lazy { App.prefs!! }

class App : Application() {

    companion object {
        var context: Context? = null
        var shareInstance: App? = null
        var prefs: PreferencesHelper? = null
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        shareInstance = this
        prefs = PreferencesHelper(applicationContext)
    }

}