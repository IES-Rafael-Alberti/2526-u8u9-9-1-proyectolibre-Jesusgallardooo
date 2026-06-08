package model

/**
 * Representa un socio del gimnasio.
 * @property id Identificador único del socio.
 * @property nombre Nombre del socio.
 * @property apellido Apellido del socio.
 * @property email Correo electrónico del socio.
 * @property telefono Número de teléfono del socio.
 * @property activo Indica si el socio está activo o dado de baja.
 */
data class Socio(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val activo: Boolean
)

