package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Clima")
data class Weather(
    @Id(assignable = true) var id: Long,
    @NameInDb("Dia")
    var day: String,
    @NameInDb("Clima")
    var weather: String,
    @NameInDb("Temperatura")
    var temperature: String
)