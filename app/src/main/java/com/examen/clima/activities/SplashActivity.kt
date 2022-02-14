package com.examen.clima.activities

import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.examen.clima.databinding.ActivitySplashBinding
import com.examen.clima.prefHelper
import com.examen.clima.utils.Constants.Companion.ENTERED
import com.examen.clima.utils.goToActivity
import com.examen.clima.utils.toast
import com.examen.clima.utils.transitionLeft
import com.examen.clima.viewModels.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instance ViewModel and call observers
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        attachObservers()

        splashViewModel.getStartSplashObservable()

        // Login with Google
        cvGoogle.setOnClickListener {
            toast("Función de Google")

            //TODO Al entrar con la sesion se debe activar bandera @hasEntered y @isLogged
        }

        // Login with Facebook
        cvFacebook.setOnClickListener {
            toast("Función de Facebook")

            //TODO Al entrar con la sesion se debe activar bandera @hasEntered y @isLogged
        }

        // Omit button function
        tvOmit.setOnClickListener {
            prefHelper.hasEntered = true
            goToActivity<MainActivity> {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            }
            transitionLeft()
        }
    }

    private fun attachObservers() {
        splashViewModel.sessionStatusObservable.observe(this, { status ->
            processStatus(status!!)
        })
    }

    private fun processStatus(status: Int) {

        //Log.i("PRUEBA", "¿Ha entrado?: ${prefHelper.hasEntered}")

        if (status == ENTERED) {
            goToActivity<MainActivity> {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            }
            transitionLeft()

        } else
            cvContainer.visibility = VISIBLE
    }
}