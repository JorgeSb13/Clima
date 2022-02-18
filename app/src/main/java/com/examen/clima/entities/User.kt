package com.examen.clima.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
@NameInDb("Usuario")
data class User(
    @Id(assignable = true) var id: Long,
    @NameInDb("Nombre")
    var name: String,
    @NameInDb("Correo")
    var email: String,
    @NameInDb("Red_social")
    var socialMedia: Int
)