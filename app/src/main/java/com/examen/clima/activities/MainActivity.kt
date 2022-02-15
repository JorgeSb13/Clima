package com.examen.clima.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.App.Companion.mGoogleSignInClient
import com.examen.clima.R
import com.examen.clima.adapters.WeatherAdapter
import com.examen.clima.databinding.ActivityMainBinding
import com.examen.clima.databinding.NavHeaderBinding
import com.examen.clima.entities.Weather
import com.examen.clima.prefHelper
import com.examen.clima.userBox
import com.examen.clima.utils.Constants.Companion.FACEBOOK
import com.examen.clima.utils.Constants.Companion.GOOGLE
import com.examen.clima.utils.alertDialog
import com.examen.clima.utils.goToActivity
import com.examen.clima.utils.toast
import com.examen.clima.utils.transitionRight
import com.examen.clima.weatherBox
import com.google.android.material.navigation.NavigationView
import com.projects.mylibrary.activities.ToolbarActivity

class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private var weatherList: ArrayList<Weather> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarToLoad(binding.toolbar.root)

        // Load the lateral menu
        setNavDrawer()
        setHeaderInfo()
        // ---------------------

        // Load the weather of next days
        mLayoutManager = LinearLayoutManager(this)
        recycler = binding.recycler

        loadWeather()
        // -----------------------------
    }

    private fun setNavDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        toggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setHeaderInfo() {
        val navBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))

        // ----- Set the information about the user -----
        val name = navBinding.tvName
        if (prefHelper.hasEntered && prefHelper.isLogged) {
            // Button for logout when you has a session
            navBinding.ivLogout.visibility = VISIBLE

            val user = userBox.query().build().findFirst()
            name.text = user!!.name

            when (user.socialMedia) {
                GOOGLE -> navBinding.ivSocialMedia.setImageResource(R.drawable.google_logo)
                FACEBOOK-> navBinding.ivSocialMedia.setImageResource(R.drawable.facebook_logo)
            }

        } else if (prefHelper.hasEntered) {
            // Button for log in a session when you're as a guest
            navBinding.ivLogin.visibility = VISIBLE
            name.text = getString(R.string.lbl_guest)
        }
        // ----------------------------------------------

        // ---- Set the information about the weather ---
        val city = navBinding.tvCity
        val temperature = navBinding.tvTemperature
        val weather = navBinding.ivWeather
        val location = navBinding.ivLocation
        // ----------------------------------------------

        navBinding.ivLogin.setOnClickListener {
            signInWithAccount()
        }
        navBinding.ivLogout.setOnClickListener {
            signOut()
        }
    }

    private fun loadWeather() {
        weatherList.clear()

        /*
        weatherBox.put(Weather(0, "Lunes", "Nublado", "25°/ 9°"))
        weatherBox.put(Weather(0, "Martes", "Soleado", "34°/ 15°"))
        weatherBox.put(Weather(0, "Miercoles", "Soleado", "32°/ 12°"))
        weatherBox.put(Weather(0, "Jueves", "Lluvioso", "23°/ 7°"))
        weatherBox.put(Weather(0, "Viernes", "Nublado", "27°/ 11°"))
        weatherBox.put(Weather(0, "Sabado", "Soleado", "37°/ 16°"))
        weatherBox.put(Weather(0, "Domingo", "Nublado", "24°/ 8°"))
         */

        for (weather in weatherBox.all) {
            weatherList.add(weather)
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = mLayoutManager
        adapter = (WeatherAdapter(weatherList))
        recycler.adapter = adapter
    }

    // ----- Add button action -----
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            // TODO Funcion de añadir nueva localizacion
        }
        return super.onOptionsItemSelected(item)
    }

    // ----- Sign in function -----
    private fun signInWithAccount() {
        alertDialog(this, "Iniciar sesión", "¿Está seguro de iniciar sesión con una cuenta?", getString(R.string.accept), { _, _ ->
            prefHelper.hasEntered = false
            goToActivity<SplashActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            transitionRight()
        }, getString(R.string.cancel))
    }

    // ----- Sign out function -----
    private fun signOut() {
        val user = userBox.query().build().findFirst()

        alertDialog(this, getString(R.string.ad_sign_out_title), getString(R.string.ad_sign_out_message), getString(R.string.accept), { _, _ ->

            when (user!!.socialMedia) {
                GOOGLE -> mGoogleSignInClient!!.signOut().addOnCompleteListener(this) {
                        clearUserInfoAndExit()
                }
                FACEBOOK-> clearUserInfoAndExit()
            }
        }, getString(R.string.cancel))
    }

    private fun clearUserInfoAndExit() {
        // ----- Reset information -----
        prefHelper.isLogged = false
        prefHelper.hasEntered = false
        userBox.removeAll()
        // -----------------------------
        goToActivity<SplashActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        transitionRight()
    }
    // -----------------------------

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}