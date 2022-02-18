package com.examen.clima.models

import com.google.gson.annotations.SerializedName

class WeatherResponse(
    @SerializedName("location")
    internal var location: Location,
    @SerializedName("current")
    internal var current: Current,
    @SerializedName("forecast")
    internal var forecast: Forecast
) {
    class Location(
        @SerializedName("name")
        internal var name: String,
        @SerializedName("region")
        internal var region: String,
        @SerializedName("localtime")
        internal var dateTime: String
    )

    class Current(
        @SerializedName("temp_c")
        internal var temperature: Float,
        @SerializedName("is_day")
        internal var isDay: Int,
        @SerializedName("condition")
        internal var condition: Condition
    )

    class Forecast(
        @SerializedName("forecastday")
        internal var forecastDay: Array<Forecastday>
    ) {
        class Forecastday(
            @SerializedName("date")
            internal var date: String,
            @SerializedName("day")
            internal var day: Day
        ) {
            class Day(
                @SerializedName("maxtemp_c")
                internal var maxTemp: Float,
                @SerializedName("mintemp_c")
                internal var minTemp: Float,
                @SerializedName("condition")
                internal var condition: Condition,
            )
        }
    }

    class Condition(
        @SerializedName("text")
        internal var text: String,
        @SerializedName("code")
        internal var code: Int
    )
}