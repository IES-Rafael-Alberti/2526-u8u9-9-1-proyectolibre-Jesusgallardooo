package org.iesra.model

import java.time.LocalDate

data class HistorialMedico(
    val id: Int,
    val idMascota: Int,
    val idVeterinario: Int,
    val fecha: LocalDate,
    val descripcion: String,
    val tratamiento: String
)
