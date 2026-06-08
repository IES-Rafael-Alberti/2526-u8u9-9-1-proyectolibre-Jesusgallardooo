package validator

import exception.ValidationException

/**
 * Valida los datos de un socio: nombre, apellido, email y teléfono mediante expresiones regulares.
 */
object SocioValidator {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val telefonoRegex = Regex("^[679][0-9]{8}$")
    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{2,50}$")

    fun validarEmail(email: String): Boolean = emailRegex.matches(email)
    fun validarTelefono(telefono: String): Boolean = telefonoRegex.matches(telefono)
    fun validarNombre(nombre: String): Boolean = nombreRegex.matches(nombre)

    fun validarSocioCompleto(nombre: String, apellido: String, email: String, telefono: String) {
        if (!validarNombre(nombre)) throw ValidationException("Nombre inválido: '$nombre'")
        if (!validarNombre(apellido)) throw ValidationException("Apellido inválido: '$apellido'")
        if (!validarEmail(email)) throw ValidationException("Email inválido: '$email'")
        if (!validarTelefono(telefono)) throw ValidationException("Teléfono inválido: '$telefono'")
    }

    fun validarParaCrear(nombre: String, apellido: String, email: String, telefono: String) {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(apellido.isNotBlank()) { "El apellido no puede estar vacío" }
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(telefono.isNotBlank()) { "El teléfono no puede estar vacío" }
        validarSocioCompleto(nombre, apellido, email, telefono)
    }
}
