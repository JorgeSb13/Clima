package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Condicion_clima")
data class WeatherCondition(
    @Id(assignable = true) var id: Long,
    @NameInDb("Condicion")
    var condition: String,
    @NameInDb("Icono")
    var icon: String
)