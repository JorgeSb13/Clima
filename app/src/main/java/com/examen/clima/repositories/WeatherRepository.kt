package com.examen.clima.repositories

import com.examen.clima.api
import com.examen.clima.models.SearchResponse
import com.examen.clima.models.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class WeatherRepository {

    interface WeatherInterface {
        fun success(response: WeatherResponse)
        fun failure(throwable: Throwable)
    }

    interface SearchInterface {
        fun success(response: ArrayList<SearchResponse>)
        fun failure(throwable: Throwable)
    }

    fun weatherRequest(location: String, weatherInterface: WeatherInterface) {

        api.getWeather(location)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .map { response ->
                response
            }
            .subscribe(
                object : ResourceObserver<WeatherResponse>() {
                    override fun onNext(response: WeatherResponse) {
                        weatherInterface.success(response)
                    }

                    override fun onError(e: Throwable) {
                        weatherInterface.failure(e)
                    }

                    override fun onComplete() {}
                }
            )
    }

    fun searchLocationRequest(location: String, searchInterface: SearchInterface) {

        api.searchLocation(location)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .map { response ->
                response
            }
            .subscribe(
                object : ResourceObserver<ArrayList<SearchResponse>>() {
                    override fun onNext(response: ArrayList<SearchResponse>) {
                        searchInterface.success(response)
                    }

                    override fun onError(e: Throwable) {
                        searchInterface.failure(e)
                    }

                    override fun onComplete() {}
                }
            )
    }

}