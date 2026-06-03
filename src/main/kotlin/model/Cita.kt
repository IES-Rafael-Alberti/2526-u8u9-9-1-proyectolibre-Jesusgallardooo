package org.iesra.model

import java.time.LocalDate
import java.time.LocalTime

data class Cita(
    val id: Int,
    val idMascota: Int,
    val idVeterinario: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val motivo: String
)
