package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Clima_actual")
data class Weather(
    @Id(assignable = true) var id: Long,
    @NameInDb("Nombre")
    var name: String,
    @NameInDb("Fecha")
    var date: String,
    @NameInDb("Hora")
    var time: String,
    @NameInDb("Es_dia")
    var isDay: Int,
    @NameInDb("Temperatura")
    var temperature: String,
    @NameInDb("Cond_clima")
    var weatherCondition: String,
    @NameInDb("Codigo")
    var code: Int
)