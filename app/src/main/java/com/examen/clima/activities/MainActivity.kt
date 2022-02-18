package com.examen.clima.activities

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.*
import com.examen.clima.App.Companion.mGoogleSignInClient
import com.examen.clima.adapters.WeatherAdapter
import com.examen.clima.databinding.ActivityMainBinding
import com.examen.clima.databinding.NavHeaderBinding
import com.examen.clima.entities.*
import com.examen.clima.models.WeatherResponse
import com.examen.clima.network.HelperUtil
import com.examen.clima.utils.*
import com.examen.clima.utils.Constants.Companion.FACEBOOK
import com.examen.clima.utils.Constants.Companion.GOOGLE
import com.examen.clima.utils.Constants.Companion.LOCATION_EXTRA
import com.examen.clima.utils.Constants.Companion.LOCATION_REQ_CODE
import com.examen.clima.utils.Constants.Companion.TAG_LOCATION
import com.examen.clima.viewModels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.projects.mylibrary.activities.ToolbarActivity
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private var forecastList: ArrayList<Forecast> = ArrayList()

    private var ubi: Ubi? = null
    private lateinit var weather: Weather

    private var anotherLocationFlag = false

    // Location functionality
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarToLoad(binding.toolbar.root)

        // ----- Initialize ViewModel and call Observers -----
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        attachObservers()

        // ----- Location functionality -----
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()

        // ----- Load screen information -----
        ubi = locationBox.query().build().findFirst()
        val location = ubi!!.locality.split(" ")

        // ----- Search weather from another location -----
        val locationExtra = intent.getStringExtra(LOCATION_EXTRA)
        if (locationExtra != null) {
            anotherLocationFlag = true
            weatherViewModel.getWeather(locationExtra)
        } else
            weatherViewModel.getWeather(location[0])

        // Load the lateral menu
        setNavDrawer()

        // Load the weather of next days
        mLayoutManager = LinearLayoutManager(this)
        recycler = binding.recycler

        // ----- Swipe to refresh information function -----
        binding.srlRefreshScreen.setOnRefreshListener {
            binding.etSearch.text.clear()

            weatherViewModel.getWeather(location[0])
            binding.srlRefreshScreen.isRefreshing = false
        }
        // -----------------------------

        // ----- Search function -----
        binding.ivSearch.setOnClickListener {
            val search = binding.etSearch.text.toString()

            if (search != "")
                weatherViewModel.searchLocation(search)
        }
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
                FACEBOOK -> navBinding.ivSocialMedia.setImageResource(R.drawable.facebook_logo)
            }

        } else if (prefHelper.hasEntered) {
            // Button for log in a session when you're as a guest
            navBinding.ivLogin.visibility = VISIBLE
            name.text = getString(R.string.lbl_guest)
        }
        // ----------------------------------------------

        // ---- Set the information about the weather ---
        if (anotherLocationFlag) {
            navBinding.ivLocation.visibility = GONE
            navBinding.tvCity.text = weather.name
        } else
            if (ubi != null) navBinding.tvCity.text = ubi!!.locality

        navBinding.tvTemperature.text = weather.temperature

        // Assign day or night for the corresponding image
        val weatherCondition = weatherConditionBox.query().equal(WeatherCondition_.id, weather.code.toLong())
            .build().findFirst()!!

        val image = if (weather.isDay == 1) "d_${weatherCondition.icon}" else "n_${weatherCondition.icon}"
        navBinding.ivWeather.setImageResource(getImage(image))
        // ----------------------------------------------

        navBinding.ivLogin.setOnClickListener { signInWithAccount() }
        navBinding.ivLogout.setOnClickListener { signOut() }
    }

    private fun loadPrincipalCardInformation() {

        if (anotherLocationFlag)
            binding.tvCity.text = weather.name
        else
            if (ubi != null) {
                binding.ivLocation.visibility = VISIBLE
                binding.tvCity.text = "${ubi!!.locality}, ${ubi!!.state}"
            }

        val weatherCondition = weatherConditionBox.query().equal(WeatherCondition_.id, weather.code.toLong())
            .build().findFirst()!!

        binding.tvDateTime.text = dateTimeFormat(weather.date, weather.time)
        binding.tvTimeZone.text = getTimeZone()
        binding.tvTemperature.text = weather.temperature
        binding.tvWeatherCondition.text = weatherCondition.condition

        // Assign day or night for the corresponding image
        val image = if (weather.isDay == 1) "d_${weatherCondition.icon}" else "n_${weatherCondition.icon}"
        binding.ivWeather.setImageResource(getImage(image))
    }

    private fun loadWeather() {
        forecastList.clear()

        for (forecast in forecastBox.all)
            forecastList.add(forecast)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = mLayoutManager
        adapter = (WeatherAdapter(forecastList))
        recycler.adapter = adapter
    }

    // ----- Sign in function -----
    private fun signInWithAccount() {
        alertDialog(this, getString(R.string.ad_sign_in_title), getString(R.string.ad_sign_in_message), getString(R.string.accept), { _, _ ->
            removeFlagsAndExitApp()
        }, getString(R.string.cancel))
    }

    // ----- Sign out function -----
    private fun signOut() {
        val user = userBox.query().build().findFirst()

        alertDialog(this, getString(R.string.ad_sign_out_title),
            getString(R.string.ad_sign_out_message), getString(R.string.accept),
            { _, _ ->

                when (user!!.socialMedia) {
                    GOOGLE -> mGoogleSignInClient!!.signOut().addOnCompleteListener(this) {
                        clearUserInfoAndExit()
                    }
                    FACEBOOK -> clearUserInfoAndExit()
                }
            },
            getString(R.string.cancel)
        )
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

    // ----- Location functionality -----
    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null) locationProcess(location)
            }
        } else
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), LOCATION_REQ_CODE)
    }

    private fun locationProcess(location: Location) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            // Remove old information and reinsert new one
            locationBox.removeAll()
            locationBox.put(Ubi(0, address[0].locality, address[0].adminArea, address[0].countryName))

        } catch (e: Exception) {
            Log.w(TAG_LOCATION, getString(R.string.error_location))
        }
    }
    // -----------------------------

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQ_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow in your app.

                    // Get location
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                            val location = it.result
                            if (location != null) locationProcess(location)
                        }
                    }

                } else {
                    alertDialog(this, getString(R.string.ad_location_permission_title),
                        getString(R.string.ad_location_permission_message), getString(R.string.accept), { _, _ ->
                        getLocationPermission()
                    }, getString(R.string.cancel), { _, _ ->
                        removeFlagsAndExitApp()
                    })
                }
                return
            }
        }
    }

    // Remove @hasEntered flag value and return to splash screen
    private fun removeFlagsAndExitApp() {
        prefHelper.hasEntered = false
        goToActivity<SplashActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        transitionRight()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    private fun insertWeatherInfoInDB(weatherResponse: WeatherResponse) {
        weatherBox.removeAll()
        forecastBox.removeAll()

        val location = weatherResponse.location
        val current = weatherResponse.current
        val condition = current.condition
        val forecastDays = weatherResponse.forecast.forecastDay
        val dateTime = dateTimeSplitter(location.dateTime)

        val name = if (anotherLocationFlag) "${location.name}, ${location.region}" else location.name

        weatherBox.put(Weather(0, name,
            dateFormat(dateTime[0]),
            timeFormat(dateTime[1]),
            current.isDay,
            temperatureFormat(current.temperature),
            condition.text,
            condition.code)
        )

        weather = weatherBox.query().build().findFirst()!!

        for (forecast in forecastDays) {
            val day = forecast.day
            val dayCondition = day.condition

            forecastBox.put(Forecast(0, weather.id,
                forecast.date, getDay(forecast.date),
                temperatureFormatNoMeasure(day.maxTemp),
                temperatureFormatNoMeasure(day.minTemp),
                dayCondition.text,
                dayCondition.code)
            )
        }

        setHeaderInfo()
        loadPrincipalCardInformation()
        loadWeather()
    }

    private fun attachObservers() {
        weatherViewModel.isLoading.observe(this, {

            if (it != null)
            // Using "root" after component name to call view reference
                enableLoading(binding.frameLoading.root, VISIBLE, it)
            else
            // Using "root" after component name to call view reference
                enableLoading(binding.frameLoading.root, INVISIBLE)
        })

        weatherViewModel.weatherSuccess.observe(this, { response ->

            if (response != null)
                insertWeatherInfoInDB(response)
        })

        weatherViewModel.weatherFailure.observe(this, { throwable ->

            if (throwable != null) {
                HelperUtil().parseError(throwable, this)
                weatherViewModel.weatherFailure.postValue(null)
            }
        })

        weatherViewModel.searchSuccess.observe(this, { response ->

            if (response != null) {
                searchBox.removeAll()

                for (search in response)
                    searchBox.put(Search(search.id.toLong(), search.name, search.region, search.country))

                goToActivity<SearchActivity>()
                transitionLeft()
            }
        })

        weatherViewModel.searchFailure.observe(this, { throwable ->
            searchBox.removeAll()

            if (throwable != null) {
                HelperUtil().parseError(throwable, this)
                weatherViewModel.weatherFailure.postValue(null)
            }
        })
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}