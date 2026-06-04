package model

import java.time.LocalDate

data class Cuota(
    val id: Long,
    val socioId: Long,
    val importe: Double,
    val fechaPago: LocalDate
)