package com.examen.clima

import android.app.Application
import android.content.Context
import com.examen.clima.BuildConfig.BASE_URL
import com.examen.clima.entities.*
import com.examen.clima.network.Api
import com.examen.clima.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.objectbox.Box
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val prefHelper: PreferencesHelper by lazy { App.prefs!! }
val boxStore: BoxStore by lazy { App.boxStore!! }
val api: Api by lazy { App.api!! }

// ----- WAREHOUSE -----
val userBox: Box<User> by lazy { App.userBox!! }
val locationBox: Box<Ubi> by lazy { App.locationBox!! }
val weatherBox: Box<Weather> by lazy { App.weatherBox!! }
val forecastBox: Box<Forecast> by lazy { App.forecastBox!! }
val searchBox: Box<Search> by lazy { App.searchBox!! }
// ----- CATALOGS ------
val weatherConditionBox: Box<WeatherCondition> by lazy { App.weatherConditionBox!! }

class App : Application() {

    companion object {
        var context: Context? = null
        var shareInstance: App? = null
        var prefs: PreferencesHelper? = null
        var boxStore: BoxStore? = null
        var api: Api? = null
        var mGoogleSignInClient: GoogleSignInClient? = null

        // ----- WAREHOUSE -----
        var userBox: Box<User>? = null
        var locationBox: Box<Ubi>? = null
        var weatherBox: Box<Weather>? = null
        var forecastBox: Box<Forecast>? = null
        var searchBox: Box<Search>? = null
        // ----- CATALOGS ------
        var weatherConditionBox: Box<WeatherCondition>? = null
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        shareInstance = this
        prefs = PreferencesHelper(applicationContext)

        boxStore = MyObjectBox.builder().androidContext(this).build()

        // ----- WAREHOUSE -----
        userBox = boxStore!!.boxFor(User::class.java)
        locationBox = boxStore!!.boxFor(Ubi::class.java)
        weatherBox = boxStore!!.boxFor(Weather::class.java)
        forecastBox = boxStore!!.boxFor(Forecast::class.java)
        searchBox = boxStore!!.boxFor(Search::class.java)
        // ----- CATALOGS ------
        weatherConditionBox = boxStore!!.boxFor(WeatherCondition::class.java)
        fillWeatherConditions()

        //Starting ObjectBox Data Browser (ONLY FOR TEST!!!)
        /*
        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(this)
            Log.i("ObjectBrowser", "Started: $started")
        }
        */

        // ----- Implemented and used for consuming APIs -----

        //Instantiate Logging interceptor
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        //Build HTTP Client
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        //Build Retrofit Client
        val retrofit = Retrofit.Builder().client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build()

        api = retrofit.create(Api::class.java)
        // ---------------------------------------------------
    }

    private fun fillWeatherConditions() {
        weatherConditionBox?.put(WeatherCondition(Constants.W_CLEAR, "Despejado", "113"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_P_CLOUDY,"Parcialmente nublado", "116"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_CLOUDY,"Nublado", "119"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_OVERCAST,"Nublado", "122"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_MIST,"Neblina", "143"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_RAIN_POSS,"Posibilidad de lluvia", "176"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_SNOW_POSS,"Posible caida nieve", "179"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_SLEET_POSS,"Posible caida de aguanieve", "182"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_FREEZING_DRIZZLE_POSS,"Posibilidad de helada", "185"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_THUNDERY_OUT_POSS,"Posible caida de rayos", "200"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_BLOWING_SNOW,"Caida de nieve", "227"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_BLIZZARD,"Nevada", "230"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_FOG,"Niebla", "248"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_FREEZING_FOG,"Niebla helada", "260"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_DRIZZLE_POSS,"Posible llovizna", "263"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_DRIZZLE,"Llovizna", "266"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_FREEZING_DRIZZLE,"Llovizna helada", "281"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_H_FREEZING_DRIZZLE,"Lluvia helada", "284"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_RAIN_POSS,"Posibilidad de lluvia ligera", "293"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_RAIN,"Lluvia ligera", "296"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_MODERATE_RAIN_1,"Lluvia moderada", "299"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_MODERATE_RAIN_2,"Lluvia moderada", "302"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_HEAVY_RAIN_1,"Lluvia fuerte", "305"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_HEAVY_RAIN_2,"Lluvia fuerte", "308"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_L_FREEZING_RAIN,"Lluvia helada ligera", "311"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_L_FREEZING_RAIN,"Lluvia helada moderada o intensa", "314"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SLEET,"Caida ligera de aguanieve", "317"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_SLEET,"Caida moderada o intensa de aguanieve", "320"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SNOW_POSS,"Posible caida ligera de nieve", "323"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SNOW,"Caida ligera de nieve", "326"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_MODERATE_SNOW_POSS,"Posible caida moderada de nieve", "329"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_MODERATE_SNOW,"Caida moderada de nieve", "332"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_HEAVY_SNOW_POSS,"Posible caida intensa de nieve", "335"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_HEAVY_SNOW,"Caida intensa de nieve", "338"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_ICE_PELLETS,"Caida de gránulos de hielo", "350"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_RAIN_S,"Lluvia ligera", "353"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_RAIN,"Lluvia moderada o intensa", "356"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_TORRENTIAL_RAIN,"Lluvia torrencial", "359"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SLEET_S,"Caida ligera de aguanieve", "362"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_SLEET_S,"Caida moderada o intensa de aguanieve", "365"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SNOW_S,"Caida ligera de nieve", "368"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_SNOW_S,"Caida moderada o intensa de nieve", "371"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_ICE_PELLETS,"Caida ligera de gránulos de hielo", "374"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_ICE_PELLETS,"Caida moderada o intensa de gránulos de hielo", "377"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_RAIN_THUNDER_POSS,"Posibilidad de lluvia ligera con truenos", "386"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_RAIN_THUNDER,"Lluvia moderada o intensa con truenos", "389"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_LIGHT_SNOW_THUNDER_POSS,"Posible caida ligera de nieve con truenos", "392"))
        weatherConditionBox?.put(WeatherCondition(Constants.W_M_OR_H_SNOW_THUNDER,"Caida moderada o intensa de nieve con truenos", "395"))
    }

}