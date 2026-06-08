package model

/**
 * Representa un entrenador del gimnasio.
 * @property id Identificador único del entrenador.
 * @property nombre Nombre del entrenador.
 * @property email Correo electrónico del entrenador.
 * @property especialidad Especialidad principal del entrenador.
 */
data class Entrenador(
    val id: Long,
    val nombre: String,
    val email: String,
    val especialidad: String
)

