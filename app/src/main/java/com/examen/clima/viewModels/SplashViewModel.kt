package com.examen.clima.viewModels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.examen.clima.prefHelper
import com.examen.clima.utils.Constants.Companion.ENTERED
import com.examen.clima.utils.Constants.Companion.NO_ENTERED

class SplashViewModel : ViewModel() {

    var sessionStatusObservable: MutableLiveData<Int> = MutableLiveData()
    private var startSplashObservable: MutableLiveData<Boolean>? = null

    private fun validateSession() {

        Handler(Looper.getMainLooper()).postDelayed({
            val hasEntered = prefHelper.hasEntered
            sessionStatusObservable.postValue(if (!hasEntered) NO_ENTERED else ENTERED)
        }, 700)
    }

    fun getStartSplashObservable(): MutableLiveData<Boolean> {

        return if (startSplashObservable == null) {
            startSplashObservable = MutableLiveData()
            validateSession()
            startSplashObservable as MutableLiveData<Boolean>

        } else
            startSplashObservable as MutableLiveData<Boolean>
    }

}