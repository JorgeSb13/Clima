package com.examen.clima.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.examen.clima.App
import com.examen.clima.R
import com.examen.clima.models.SearchResponse
import com.examen.clima.models.WeatherResponse
import com.examen.clima.repositories.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    var isLoading = MutableLiveData<String>()
    var weatherSuccess = MutableLiveData<WeatherResponse>()
    var weatherFailure = MutableLiveData<Throwable>()

    var searchSuccess = MutableLiveData<ArrayList<SearchResponse>>()
    var searchFailure = MutableLiveData<Throwable>()

    fun getWeather(location: String) {
        isLoading.value = App.context!!.resources.getString(R.string.loading_getting_information)

        weatherRepository.weatherRequest(location, object : WeatherRepository.WeatherInterface {
            override fun success(response: WeatherResponse) {
                isLoading.value = null
                weatherSuccess.value = response
            }

            override fun failure(throwable: Throwable) {
                isLoading.value = null
                weatherFailure.value = throwable
            }
        })
    }

    fun searchLocation(location: String) {
        isLoading.value = App.context!!.resources.getString(R.string.loading_searching)

        weatherRepository.searchLocationRequest(location, object : WeatherRepository.SearchInterface {
            override fun success(response: ArrayList<SearchResponse>) {
                isLoading.value = null
                searchSuccess.value = response
            }

            override fun failure(throwable: Throwable) {
                isLoading.value = null
                searchFailure.value = throwable
            }
        })
    }

}