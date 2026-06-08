package validator

import exception.ValidationException
import java.time.LocalDate

/**
 * Valida los datos de una inscripción: IDs de socio y actividad, y fecha de inscripción.
 */
object InscripcionValidator {

    fun validarFechas(fechaInscripcion: LocalDate): Boolean = !fechaInscripcion.isAfter(LocalDate.now())

    fun validarInscripcion(socioId: Long, actividadId: Long, fechaInscripcion: LocalDate) {
        require(socioId > 0) { "El ID del socio debe ser positivo" }
        require(actividadId > 0) { "El ID de la actividad debe ser positivo" }
        if (!validarFechas(fechaInscripcion)) throw ValidationException("Fecha futura no permitida")
    }
}
