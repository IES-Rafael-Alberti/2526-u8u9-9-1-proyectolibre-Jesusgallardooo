package model

import java.time.LocalDate

data class Inscripcion(
    val id: Long,
    val socioId: Long,
    val actividadId: Long,
    val fechaInscripcion: LocalDate
)