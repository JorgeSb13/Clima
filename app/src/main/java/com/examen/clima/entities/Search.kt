package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Busqueda")
data class Search(
    @Id(assignable = true) var id: Long,
    @NameInDb("Localidad")
    var name: String,
    @NameInDb("Region")
    var region: String,
    @NameInDb("Pais")
    var country: String
)