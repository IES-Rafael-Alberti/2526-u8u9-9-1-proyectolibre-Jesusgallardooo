package validator

import exception.ValidationException

/**
 * Valida los datos de una actividad: nombre (con regex) y número de plazas.
 */
object ActividadValidator {

    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{3,50}$")

    fun validarNombre(nombre: String): Boolean = nombreRegex.matches(nombre)
    fun validarPlazas(plazasMaximas: Int): Boolean = plazasMaximas in 1..100

    fun validarActividad(nombre: String, plazasMaximas: Int) {
        if (!validarNombre(nombre)) throw ValidationException("Nombre inválido: '$nombre'")
        if (!validarPlazas(plazasMaximas)) throw ValidationException("Plazas inválidas: $plazasMaximas")
    }
}
