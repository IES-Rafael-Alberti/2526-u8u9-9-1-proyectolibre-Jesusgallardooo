package validator

object SocioValidator {

    // Regex para email (formato estándar)
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Regex para teléfono español (9 dígitos, empieza por 6,7,9)
    private val telefonoRegex = Regex("^[679][0-9]{8}$")

    // Regex para nombre/apellido (solo letras, espacios, y caracteres españoles)
    private val nombreRegex = Regex("^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\\s]{2,50}$")

    fun validarEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun validarTelefono(telefono: String): Boolean {
        return telefonoRegex.matches(telefono)
    }

    fun validarNombre(nombre: String): Boolean {
        return nombreRegex.matches(nombre)
    }

    fun validarSocioCompleto(nombre: String, apellido: String, email: String, telefono: String) {
        // Validar nombre
        if (!validarNombre(nombre)) {
            throw ValidationException("Nombre inválido: '$nombre'. Debe tener 2-50 caracteres y solo letras")
        }

        // Validar apellido
        if (!validarNombre(apellido)) {
            throw ValidationException("Apellido inválido: '$apellido'. Debe tener 2-50 caracteres y solo letras")
        }

        // Validar email
        if (!validarEmail(email)) {
            throw ValidationException("Email inválido: '$email'. Formato requerido: usuario@dominio.com")
        }

        // Validar teléfono
        if (!validarTelefono(telefono)) {
            throw ValidationException("Teléfono inválido: '$telefono'. Debe ser 9 dígitos empezando por 6,7 o 9")
        }
    }

    // Versión simple para crear socio (mismo método pero más claro)
    fun validarParaCrear(nombre: String, apellido: String, email: String, telefono: String) {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(apellido.isNotBlank()) { "El apellido no puede estar vacío" }
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(telefono.isNotBlank()) { "El teléfono no puede estar vacío" }

        validarSocioCompleto(nombre, apellido, email, telefono)
    }
}