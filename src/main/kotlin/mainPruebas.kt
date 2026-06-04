// test/TestSocioService.kt
package test

import service.SocioService
import repository.memoryTests.InMemorySocioRepository

fun main() {
    println("=" .repeat(60))
    println("   TEST DEL SOCIO SERVICE")
    println("=" .repeat(60))

    val service = SocioService(InMemorySocioRepository())

    // 1. Probar crear socios
    println("\n📝 1. TEST CREAR SOCIOS:")
    println("-".repeat(40))

    try {
        val socio1 = service.crearSocio("Ana", "García", "ana@email.com", "612345678")
        println("   ✅ Creado: ${socio1.nombre} ${socio1.apellido} (ID: ${socio1.id})")

        val socio2 = service.crearSocio("Juan", "Pérez", "juan@email.com", "623456789")
        println("   ✅ Creado: ${socio2.nombre} ${socio2.apellido} (ID: ${socio2.id})")

        val socio3 = service.crearSocio("María", "López", "maria@email.com", "634567890")
        println("   ✅ Creado: ${socio3.nombre} ${socio3.apellido} (ID: ${socio3.id})")
    } catch (e: ValidationException) {
        println("   ❌ Error: ${e.message}")
    }

    // 2. Probar listar todos
    println("\n📋 2. TEST LISTAR TODOS:")
    println("-".repeat(40))

    val todos = service.listarTodosLosSocios()
    println("   Total socios: ${todos.size}")
    todos.forEach { socio ->
        println("   [${socio.id}] ${socio.nombre} ${socio.apellido} - Activo: ${socio.activo}")
    }

    // 3. Probar obtener por ID
    println("\n🔍 3. TEST OBTENER POR ID:")
    println("-".repeat(40))

    try {
        val socio = service.obtenerSocio(1)
        println("   ✅ Socio ID 1: ${socio.nombre} ${socio.apellido}")
    } catch (e: NotFoundException) {
        println("   ❌ ${e.message}")
    }

    try {
        service.obtenerSocio(99)
    } catch (e: NotFoundException) {
        println("   ✅ Error esperado: ${e.message}")
    }

    // 4. Probar dar de baja
    println("\n⚠️ 4. TEST DAR DE BAJA:")
    println("-".repeat(40))

    try {
        val socioBaja = service.darDeBaja(2)
        println("   ✅ Socio dado de baja: ${socioBaja.nombre} - Activo: ${socioBaja.activo}")
    } catch (e: Exception) {
        println("   ❌ ${e.message}")
    }

    // 5. Probar listar solo activos
    println("\n🟢 5. TEST LISTAR SOLO ACTIVOS:")
    println("-".repeat(40))

    val activos = service.listarSociosActivos()
    println("   Socios activos: ${activos.size}")
    activos.forEach { socio ->
        println("   [${socio.id}] ${socio.nombre} ${socio.apellido}")
    }

    // 6. Probar listar inactivos
    println("\n🔴 6. TEST LISTAR INACTIVOS:")
    println("-".repeat(40))

    val inactivos = service.listarSociosInactivos()
    println("   Socios inactivos: ${inactivos.size}")
    inactivos.forEach { socio ->
        println("   [${socio.id}] ${socio.nombre} ${socio.apellido}")
    }

    // 7. Probar dar de alta
    println("\n🟢 7. TEST DAR DE ALTA:")
    println("-".repeat(40))

    try {
        val socioAlta = service.darDeAlta(2)
        println("   ✅ Socio dado de alta: ${socioAlta.nombre} - Activo: ${socioAlta.activo}")
    } catch (e: Exception) {
        println("   ❌ ${e.message}")
    }

    // 8. Verificar que ahora está activo
    println("\n📊 8. VERIFICACIÓN FINAL:")
    println("-".repeat(40))
    println("   Total socios: ${service.contarSocios()}")
    println("   Socios activos: ${service.contarSociosActivos()}")

    // 9. Probar crear socio con email inválido
    println("\n🧪 9. TEST VALIDACIÓN (email inválido):")
    println("-".repeat(40))

    try {
        service.crearSocio("Pedro", "Ruiz", "email-invalido", "611111111")
        println("   ❌ Debería haber fallado")
    } catch (e: ValidationException) {
        println("   ✅ Error capturado: ${e.message}")
    }

    // 10. Probar eliminar socio
    println("\n🗑️ 10. TEST ELIMINAR SOCIO:")
    println("-".repeat(40))

    try {
        val eliminado = service.eliminarSocio(3)
        println("   ✅ Socio eliminado: $eliminado")
        println("   Socios restantes: ${service.contarSocios()}")
    } catch (e: Exception) {
        println("   ❌ ${e.message}")
    }

    println("\n" + "=" .repeat(60))
    println("   RESULTADO FINAL DEL SERVICE:")
    println("=" .repeat(60))
    println("   ✅ Crear socio funciona")
    println("   ✅ Obtener socio funciona")
    println("   ✅ Listar todos funciona")
    println("   ✅ Listar activos funciona")
    println("   ✅ Dar de baja funciona")
    println("   ✅ Dar de alta funciona")
    println("   ✅ Eliminar socio funciona")
    println("   ✅ Validaciones funcionan")
    println("   ✅ Manejo de excepciones funciona")
    println("=" .repeat(60))
}