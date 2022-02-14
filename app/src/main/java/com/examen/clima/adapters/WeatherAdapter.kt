package com.examen.clima.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.R
import com.examen.clima.entities.Weather
import com.examen.clima.utils.inflate
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(private val data: ArrayList<Weather>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_weather))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) = with(itemView) {

            tvDay.text = weather.day
            tvWeather.text = weather.weather
            tvTemperature.text = weather.temperature
        }
    }

}