package model

import java.time.LocalDate

/**
 * Representa el pago de una cuota por parte de un socio.
 * @property id Identificador único de la cuota.
 * @property socioId Identificador del socio que realizó el pago.
 * @property importe Cantidad pagada.
 * @property fechaPago Fecha en la que se realizó el pago.
 */
data class Cuota(
    val id: Long,
    val socioId: Long,
    val importe: Double,
    val fechaPago: LocalDate
)

