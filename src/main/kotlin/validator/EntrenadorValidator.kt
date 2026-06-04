package validator

// validator/EntrenadorValidator.kt

object EntrenadorValidator {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{2,50}$")

    fun validarEmail(email: String): Boolean = emailRegex.matches(email)
    fun validarNombre(nombre: String): Boolean = nombreRegex.matches(nombre)

    fun validarEntrenador(nombre: String, email: String, especialidad: String) {
        // Validar nombre
        if (!validarNombre(nombre)) {
            throw ValidationException("Nombre inválido: '$nombre'. Debe tener 2-50 caracteres y solo letras")
        }

        // Validar email
        if (!validarEmail(email)) {
            throw ValidationException("Email inválido: '$email'. Formato: usuario@dominio.com")
        }

        // Validar especialidad
        require(especialidad.isNotBlank()) { "La especialidad no puede estar vacía" }
        if (especialidad.length < 3) {
            throw ValidationException("Especialidad demasiado corta: mínimo 3 caracteres")
        }
    }
}