package com.examen.clima.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.R
import com.examen.clima.entities.Forecast
import com.examen.clima.entities.WeatherCondition_
import com.examen.clima.utils.getImage
import com.examen.clima.utils.inflate
import com.examen.clima.weatherConditionBox
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(private val data: ArrayList<Forecast>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_weather))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(forecast: Forecast) = with(itemView) {

            val weatherCondition = weatherConditionBox.query().equal(WeatherCondition_.id, forecast.code.toLong())
                .build().findFirst()!!

            tvDay.text = forecast.day
            tvTemperature.text = "${forecast.max_temp}/ ${forecast.min_temp}"

            val image = "d_${weatherCondition.icon}"
            ivWeather.setImageResource(getImage(image))
        }
    }

}