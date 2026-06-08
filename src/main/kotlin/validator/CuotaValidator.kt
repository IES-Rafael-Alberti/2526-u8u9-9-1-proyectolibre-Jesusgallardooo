package validator

import exception.ValidationException
import java.time.LocalDate

/**
 * Valida los datos de una cuota: importe, fecha de pago e ID del socio.
 */
object CuotaValidator {

    fun validarImporte(importe: Double): Boolean = importe > 0 && importe <= 1000
    fun validarFecha(fechaPago: LocalDate): Boolean = !fechaPago.isAfter(LocalDate.now())

    fun validarCuota(socioId: Long, importe: Double, fechaPago: LocalDate) {
        require(socioId > 0) { "El ID del socio debe ser positivo" }
        if (!validarImporte(importe)) throw ValidationException("Importe inválido: $importe€")
        if (!validarFecha(fechaPago)) throw ValidationException("Fecha inválida: $fechaPago")
    }
}
