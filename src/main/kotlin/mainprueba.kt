// test/TestClearTables.kt
package test

import repository.sql.DatabaseManager
import repository.sql.SqlSocioRepository
import repository.sql.SqlActividadRepository
import repository.sql.SqlEntrenadorRepository
import repository.sql.SqlInscripcionRepository
import model.*
import java.time.LocalDate

fun main() {
    println("=== TEST VACIAR TABLAS ===\n")

    DatabaseManager.initDatabase()

    val socioRepo = SqlSocioRepository()
    val actividadRepo = SqlActividadRepository()
    val inscripcionRepo = SqlInscripcionRepository()

    // 1. Insertar datos de prueba
    println("1. Insertando datos de prueba...")
    val socio = socioRepo.create(Socio(1, "Prueba", "Test", "test@email.com", "600000000", true))
    val actividad = actividadRepo.create(Actividad(1, "Test Actividad", 10))
    inscripcionRepo.create(Inscripcion(1, socio.id, actividad.id, LocalDate.now()))
    println("   Datos insertados\n")

    // 2. Verificar que hay datos
    println("2. Verificando datos antes de vaciar...")
    println("   Socios: ${socioRepo.findAll().size}")
    println("   Actividades: ${actividadRepo.findAll().size}")
    println("   Inscripciones: ${inscripcionRepo.findAll().size}\n")

    // 3. Vaciar todas las tablas
    println("3. Vaciando todas las tablas...")
    DatabaseManager.clearAllTables()
    println()

    // 4. Verificar que están vacías
    println("4. Verificando datos despues de vaciar...")
    println("   Socios: ${socioRepo.findAll().size}")
    println("   Actividades: ${actividadRepo.findAll().size}")
    println("   Inscripciones: ${inscripcionRepo.findAll().size}")

    // 5. Insertar nuevo socio para verificar que el ID empieza en 1
    println("\n5. Insertando nuevo socio...")
    val nuevoSocio = socioRepo.create(Socio(0, "Nuevo", "Usuario", "nuevo@email.com", "611111111", true))
    println("   Nuevo ID: ${nuevoSocio.id} (deberia ser 1)")

    DatabaseManager.closeConnection()

    println("\n=== TEST COMPLETADO ===")
}