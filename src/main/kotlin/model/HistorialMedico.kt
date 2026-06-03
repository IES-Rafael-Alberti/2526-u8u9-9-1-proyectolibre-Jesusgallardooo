package org.iesra.model

import java.util.Date

data class HistorialMedico(
    val id: String? = null,
    val idMascota: Int,
    val descripcion: String,
    val diagnostico: String,
    val tratamiento: String,
    val fecha: Date
)
