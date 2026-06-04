package validator

import exception.ValidationException


object ActividadValidator {

    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{3,50}$")

    fun validarNombre(nombre: String): Boolean {
        return nombreRegex.matches(nombre)
    }

    fun validarPlazas(plazasMaximas: Int): Boolean {
        return plazasMaximas in 1..25
    }

    fun validarActividad(nombre: String, plazasMaximas: Int) {
        // Validar nombre
        if (!validarNombre(nombre)) {
            throw ValidationException("Nombre de actividad inválido: '$nombre'. Debe tener 3-50 caracteres y solo letras")
        }

        // Validar plazas
        if (!validarPlazas(plazasMaximas)) {
            throw ValidationException("Plazas máximas inválidas: $plazasMaximas. Debe estar entre 1 y 100")
        }
    }
}