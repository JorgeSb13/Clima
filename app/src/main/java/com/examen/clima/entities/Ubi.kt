package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Ubicacion")
data class Ubi(
    @Id(assignable = true) var id: Long,
    @NameInDb("Localidad")
    var locality: String,
    @NameInDb("Estado")
    var state: String,
    @NameInDb("Pais")
    var country: String
)