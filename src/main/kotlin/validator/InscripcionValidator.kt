package validator

import exception.ValidationException

// validator/InscripcionValidator.kt

import java.time.LocalDate

object InscripcionValidator {

    fun validarFechas(fechaInscripcion: LocalDate): Boolean {
        val hoy = LocalDate.now()
        return !fechaInscripcion.isAfter(hoy) // No puede ser futuro
    }

    fun validarInscripcion(socioId: Long, actividadId: Long, fechaInscripcion: LocalDate) {
        // Validar IDs
        require(socioId > 0) { "El ID del socio debe ser positivo" }
        require(actividadId > 0) { "El ID de la actividad debe ser positivo" }

        // Validar fecha
        if (!validarFechas(fechaInscripcion)) {
            throw ValidationException("Fecha de inscripción inválida: $fechaInscripcion. No puede ser futura")
        }
    }
}