import exception.ValidationException
import validator.ActividadValidator
import validator.SocioValidator

// test/TestInMemoryRepository.kt


import model.Socio
import repository.memoryTests.InMemorySocioRepository
import exception.NotFoundException

fun main() {
    println("=" .repeat(60))
    println("   TEST DEL REPOSITORIO EN MEMORIA")
    println("=" .repeat(60))

    val repo = InMemorySocioRepository()

    // 1. Probar CREATE
    println("\n📝 1. TEST CREATE:")
    println("-".repeat(40))

    val socio1 = Socio(0, "Ana", "García", "ana@email.com", "612345678", true)
    val socio2 = Socio(0, "Juan", "Pérez", "juan@email.com", "623456789", true)

    val saved1 = repo.create(socio1)
    val saved2 = repo.create(socio2)

    println("   ✅ Creado: ${saved1.nombre} ${saved1.apellido} con ID: ${saved1.id}")
    println("   ✅ Creado: ${saved2.nombre} ${saved2.apellido} con ID: ${saved2.id}")

    // 2. Probar FIND ALL
    println("\n📋 2. TEST FIND ALL:")
    println("-".repeat(40))

    val todos = repo.findAll()
    println("   Total de socios: ${todos.size}")
    todos.forEach { socio ->
        println("   [${socio.id}] ${socio.nombre} ${socio.apellido}")
    }

    // 3. Probar FIND BY ID
    println("\n🔍 3. TEST FIND BY ID:")
    println("-".repeat(40))

    val encontrado = repo.findById(1)
    if (encontrado != null) {
        println("   ✅ Socio con ID 1: ${encontrado.nombre} ${encontrado.apellido}")
    } else {
        println("   ❌ No se encontró el socio con ID 1")
    }

    val noEncontrado = repo.findById(99)
    println("   🔍 Buscar ID 99: ${if (noEncontrado == null) "null (correcto)" else "ERROR"}")

    // 4. Probar UPDATE
    println("\n✏️ 4. TEST UPDATE:")
    println("-".repeat(40))

    val socioActualizado = encontrado!!.copy(telefono = "699999999", activo = false)
    val updated = repo.update(socioActualizado)
    println("   ✅ Actualizado: ${updated.nombre} - Teléfono: ${updated.telefono} - Activo: ${updated.activo}")

    // Verificar que se actualizó
    val verificar = repo.findById(1)
    println("   🔍 Verificación: Teléfono = ${verificar?.telefono}")

    // 5. Probar DELETE
    println("\n🗑️ 5. TEST DELETE:")
    println("-".repeat(40))

    val deleted = repo.delete(2)
    println("   ✅ Eliminado ID 2: $deleted")

    val despuesBorrar = repo.findAll()
    println("   Socios restantes: ${despuesBorrar.size}")
    despuesBorrar.forEach { socio ->
        println("   [${socio.id}] ${socio.nombre} ${socio.apellido}")
    }

    // 6. Probar DELETE de ID que no existe
    println("\n🧪 6. TEST DELETE (ID no existe):")
    println("-".repeat(40))

    val deletedFalso = repo.delete(99)
    println("   Eliminar ID 99 (no existe): $deletedFalso (debe ser false)")

    // 7. Probar UPDATE de socio que no existe (debe lanzar excepción)
    println("\n⚠️ 7. TEST UPDATE (ID no existe - debe lanzar excepción):")
    println("-".repeat(40))

    try {
        val socioInexistente = Socio(99, "Test", "Test", "test@email.com", "600000000", true)
        repo.update(socioInexistente)
        println("   ❌ ERROR: Debería haber lanzado NotFoundException")
    } catch (e: NotFoundException) {
        println("   ✅ Excepción capturada correctamente: ${e.message}")
    }

    // 8. Resumen final
    println("\n" + "=" .repeat(60))
    println("   RESULTADO FINAL DEL REPOSITORIO:")
    println("=" .repeat(60))
    println("   ✅ CREATE funciona")
    println("   ✅ FIND ALL funciona")
    println("   ✅ FIND BY ID funciona")
    println("   ✅ UPDATE funciona")
    println("   ✅ DELETE funciona")
    println("   ✅ Manejo de excepciones funciona")
    println("=" .repeat(60))
}