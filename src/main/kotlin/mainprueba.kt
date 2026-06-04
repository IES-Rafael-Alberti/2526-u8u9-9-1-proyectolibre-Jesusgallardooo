// test/TestDatabaseManager.kt
package test

import repository.sql.DatabaseManager
import java.sql.ResultSet

fun main() {
    println("=== TEST DATABASE MANAGER ===\n")

    // 1. Inicializar base de datos
    println("1. Inicializando base de datos...")
    DatabaseManager.initDatabase()
    println("   OK\n")

    // 2. Probar conexion
    println("2. Probando conexion...")
    val conn = DatabaseManager.getConnection()
    if (!conn.isClosed) {
        println("   Conexion establecida correctamente\n")
    } else {
        println("   ERROR: No se pudo conectar\n")
        return
    }

    // 3. Verificar que las tablas se crearon
    println("3. Verificando tablas creadas...")
    val tables = listOf("socios", "actividades", "entrenadores", "inscripciones")

    for (table in tables) {
        try {
            val rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM $table")
            rs.next()
            val count = rs.getInt(1)
            println("   Tabla '$table' existe - $count registros")
            rs.close()
        } catch (e: Exception) {
            println("   ERROR: Tabla '$table' no existe - ${e.message}")
        }
    }
    println()

    // 4. Insertar un socio de prueba
    println("4. Insertando socio de prueba...")
    try {
        val sql = "INSERT INTO socios (nombre, apellido, email, telefono, activo) VALUES (?, ?, ?, ?, ?)"
        val pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
        pstmt.setString(1, "Prueba")
        pstmt.setString(2, "Test")
        pstmt.setString(3, "prueba@email.com")
        pstmt.setString(4, "600000000")
        pstmt.setBoolean(5, true)

        val filas = pstmt.executeUpdate()
        val generatedKeys = pstmt.generatedKeys

        if (generatedKeys.next()) {
            val id = generatedKeys.getLong(1)
            println("   Socio insertado correctamente con ID: $id\n")
        }
        pstmt.close()
    } catch (e: Exception) {
        println("   ERROR al insertar: ${e.message}\n")
    }

    // 5. Consultar socios
    println("5. Consultando socios...")
    try {
        val rs = conn.createStatement().executeQuery("SELECT * FROM socios")
        while (rs.next()) {
            println("   [${rs.getLong("id")}] ${rs.getString("nombre")} ${rs.getString("apellido")} - ${rs.getString("email")}")
        }
        rs.close()
        println()
    } catch (e: Exception) {
        println("   ERROR al consultar: ${e.message}\n")
    }

    // 6. Cerrar conexion
    println("6. Cerrando conexion...")
    DatabaseManager.closeConnection()
    println("   OK\n")

    println("=== TEST COMPLETADO ===")
}