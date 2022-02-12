package com.examen.clima

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.examen.clima.utils.Constants.Companion.SHARED_FILE_NAME

class PreferencesHelper(context: Context) {

    private val fileName = SHARED_FILE_NAME
    private val prefs = context.getSharedPreferences(fileName, MODE_PRIVATE)
}