package com.examen.clima

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.examen.clima.utils.Constants.Companion.KEY_HAS_ENTERED
import com.examen.clima.utils.Constants.Companion.KEY_IS_LOGGED
import com.examen.clima.utils.Constants.Companion.SHARED_FILE_NAME

class PreferencesHelper(context: Context) {

    private val fileName = SHARED_FILE_NAME
    private val prefs = context.getSharedPreferences(fileName, MODE_PRIVATE)

    var hasEntered: Boolean
        get() = prefs.getBoolean(KEY_HAS_ENTERED, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_ENTERED, value).apply()

    var isLogged: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED, value).apply()
}