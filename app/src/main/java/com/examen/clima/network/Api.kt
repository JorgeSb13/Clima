package com.examen.clima.network

import com.examen.clima.models.SearchResponse
import com.examen.clima.models.WeatherResponse
import com.examen.clima.utils.Constants.Companion.API_KEY
import com.examen.clima.utils.Constants.Companion.N_FORECAST_DAYS
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("forecast.json?key=${API_KEY}&days=${N_FORECAST_DAYS}&aqi=no&alerts=no")
    fun getWeather(
        @Query("q") location: String
    ): Observable<WeatherResponse>

    @GET("search.json?key=${API_KEY}")
    fun searchLocation(
        @Query("q") location: String
    ): Observable<SearchResponse>
}