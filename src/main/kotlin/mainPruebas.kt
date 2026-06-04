import exception.ValidationException
import validator.ActividadValidator
import validator.SocioValidator

// test/TestValidators.kt (temporal, luego lo borras)
fun main() {
    println("=== Probando validadores ===\n")

    // Probar SocioValidator
    println("1. Probando SocioValidator:")
    try {
        SocioValidator.validarParaCrear("Ana", "García", "ana@email.com", "612345678")
        println("   ✅ Socio válido")
    } catch (e: ValidationException) {
        println("   ❌ ${e.message}")
    }

    try {
        SocioValidator.validarParaCrear("A", "García", "ana@email.com", "612345678")
    } catch (e: ValidationException) {
        println("   ❌ Error esperado: ${e.message}")
    }

    // Probar ActividadValidator
    println("\n2. Probando ActividadValidator:")
    try {
        ActividadValidator.validarActividad("Yoga", 15)
        println("   ✅ Actividad válida")
    } catch (e: ValidationException) {
        println("   ❌ ${e.message}")
    }

    try {
        ActividadValidator.validarActividad("Yoga", 150)
    } catch (e: ValidationException) {
        println("   ❌ Error esperado: ${e.message}")
    }

    // Probar ejemplos de emails válidos e inválidos
    println("\n3. Ejemplos de emails:")
    val emailsValidos = listOf(
        "usuario@email.com",
        "user.name@dominio.es",
        "user+tag@email.co.uk"
    )

    emailsValidos.forEach { email ->
        println("   '$email' -> ${if(SocioValidator.validarEmail(email)) "✅ válido" else "❌ inválido"}")
    }

    val emailsInvalidos = listOf(
        "usuario@",
        "@dominio.com",
        "usuario@dominio",
        "usuario dominio.com"
    )

    emailsInvalidos.forEach { email ->
        println("   '$email' -> ${if(SocioValidator.validarEmail(email)) "✅ válido" else "❌ inválido"}")
    }

    println("\n=== Fin de pruebas ===")
}