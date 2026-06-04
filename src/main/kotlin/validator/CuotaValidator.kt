package validator
// validator/CuotaValidator.kt

import java.time.LocalDate

object CuotaValidator {

    fun validarImporte(importe: Double): Boolean {
        return importe > 0 && importe <= 1000
    }

    fun validarFecha(fechaPago: LocalDate): Boolean {
        val hoy = LocalDate.now()
        return !fechaPago.isAfter(hoy) // No puede ser fecha futura
    }

    fun validarCuota(socioId: Long, importe: Double, fechaPago: LocalDate) {
        // Validar socioId
        require(socioId > 0) { "El ID del socio debe ser positivo" }

        // Validar importe
        if (!validarImporte(importe)) {
            throw ValidationException("Importe inválido: $importe€. Debe estar entre 0 y 1000€")
        }

        // Validar fecha
        if (!validarFecha(fechaPago)) {
            throw ValidationException("Fecha inválida: $fechaPago. No puede ser una fecha futura")
        }
    }
}