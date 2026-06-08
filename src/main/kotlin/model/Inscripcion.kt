package model

import java.time.LocalDate

/**
 * Representa la inscripción de un socio en una actividad.
 * @property id Identificador único de la inscripción.
 * @property socioId Identificador del socio inscrito.
 * @property actividadId Identificador de la actividad en la que se inscribe.
 * @property fechaInscripcion Fecha en la que se realizó la inscripción.
 */
data class Inscripcion(
    val id: Long,
    val socioId: Long,
    val actividadId: Long,
    val fechaInscripcion: LocalDate
)

