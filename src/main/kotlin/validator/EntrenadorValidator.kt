package validator

import exception.ValidationException

/**
 * Valida los datos de un entrenador: nombre, email y especialidad.
 */
object EntrenadorValidator {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{2,50}$")

    fun validarEmail(email: String): Boolean = emailRegex.matches(email)
    fun validarNombre(nombre: String): Boolean = nombreRegex.matches(nombre)

    fun validarEntrenador(nombre: String, email: String, especialidad: String) {
        if (!validarNombre(nombre)) throw ValidationException("Nombre inválido: '$nombre'")
        if (!validarEmail(email)) throw ValidationException("Email inválido: '$email'")
        require(especialidad.isNotBlank()) { "La especialidad no puede estar vacía" }
        if (especialidad.length < 3) throw ValidationException("Especialidad demasiado corta")
    }
}
