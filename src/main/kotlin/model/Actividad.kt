package model

/**
 * Representa una actividad ofrecida por el gimnasio.
 * @property id Identificador único de la actividad.
 * @property nombre Nombre de la actividad.
 * @property plazasMaximas Número máximo de plazas disponibles.
 */
data class Actividad(
    val id: Long,
    val nombre: String,
    val plazasMaximas: Int
)

