package com.examen.clima

import android.app.Application
import android.content.Context
import android.util.Log
import com.examen.clima.entities.MyObjectBox
import com.examen.clima.entities.User
import com.examen.clima.entities.Weather
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

val prefHelper: PreferencesHelper by lazy { App.prefs!! }
val boxStore: BoxStore by lazy { App.boxStore!! }

// ----- WAREHOUSE -----
val userBox: Box<User> by lazy { App.userBox!! }
val weatherBox: Box<Weather> by lazy { App.weatherBox!! }

class App : Application() {

    companion object {
        var context: Context? = null
        var shareInstance: App? = null
        var prefs: PreferencesHelper? = null
        var boxStore: BoxStore? = null

        // ----- WAREHOUSE -----
        var userBox: Box<User>? = null
        var weatherBox: Box<Weather>? = null
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        shareInstance = this
        prefs = PreferencesHelper(applicationContext)

        boxStore = MyObjectBox.builder().androidContext(this).build()

        // ----- WAREHOUSE -----
        userBox = boxStore!!.boxFor(User::class.java)
        weatherBox = boxStore!!.boxFor(Weather::class.java)

        //Starting ObjectBox Data Browser (ONLY FOR TEST!!!)
        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(this)
            Log.i("ObjectBrowser", "Started: $started")
        }
    }

}