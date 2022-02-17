package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Pronostico_futuro")
data class Forecast(
    @Id(assignable = true) var id: Long,
    @NameInDb("id_localizacion")
    var id_location: Long,
    @NameInDb("Fecha")
    var date: String,
    @NameInDb("Dia")
    var day: String,
    @NameInDb("Temp_max")
    var max_temp: String,
    @NameInDb("Temp_min")
    var min_temp: String,
    @NameInDb("Cond_clima")
    var weatherCondition: String,
    @NameInDb("Codigo")
    var code: Int
)