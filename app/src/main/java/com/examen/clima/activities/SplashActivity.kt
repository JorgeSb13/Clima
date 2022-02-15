package com.examen.clima.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.examen.clima.App.Companion.mGoogleSignInClient
import com.examen.clima.R
import com.examen.clima.databinding.ActivitySplashBinding
import com.examen.clima.entities.User
import com.examen.clima.prefHelper
import com.examen.clima.userBox
import com.examen.clima.utils.*
import com.examen.clima.utils.Constants.Companion.ENTERED
import com.examen.clima.utils.Constants.Companion.GOOGLE
import com.examen.clima.utils.Constants.Companion.RC_SIGN_IN
import com.examen.clima.viewModels.SplashViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

        // ----- Login with Google -----
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        cvGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        // -----------------------------

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

        if (status == ENTERED) {
            goToActivity<MainActivity> {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            }
            transitionLeft()

        } else
            cvContainer.visibility = VISIBLE
    }

    // ----- Methods used to login with Google -----
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Signed in successfully, show authenticated UI.
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val name = acct.displayName
                val email = acct.email
                userBox.put(User(0, name.toString(), email.toString(), GOOGLE))

                prefHelper.isLogged = true
                prefHelper.hasEntered = true
                processStatus(ENTERED)

            } else
                simpleAlertDialog(this, getString(R.string.ad_error_occurred_title), getString(R.string.ad_error_occurred_message), getString(R.string.accept))

        } catch (e: ApiException) {
            Log.w("GOOGLE", "signInResult:failed code=" + e.statusCode)
        }
    }
    // ---------------------------------------------

}