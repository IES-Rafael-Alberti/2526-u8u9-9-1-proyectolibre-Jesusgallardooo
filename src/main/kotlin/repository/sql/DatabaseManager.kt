// repository/sql/DatabaseManager.kt
package repository.sql

import io.github.cdimascio.dotenv.dotenv
import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {

    private val dotenv = dotenv()

    private val URL = dotenv["H2_URL"]
    private val USER = dotenv["H2_USER"]
    private val PASSWORD = dotenv["H2_PASSWORD"]

    private var connection: Connection? = null

    fun getConnection(): Connection {
        if (connection == null || connection!!.isClosed) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD)
        }
        return connection!!
    }

    fun initDatabase() {
        try {
            val conn = getConnection()

            // Crear tabla socios
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS socios (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    telefono VARCHAR(15) NOT NULL,
                    activo BOOLEAN DEFAULT TRUE
                )
            """)

            // Crear tabla actividades
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS actividades (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    plazas_maximas INT NOT NULL
                )
            """)

            // Crear tabla entrenadores
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS entrenadores (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    especialidad VARCHAR(50) NOT NULL
                )
            """)

            // Crear tabla inscripciones
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS inscripciones (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    socio_id BIGINT NOT NULL,
                    actividad_id BIGINT NOT NULL,
                    fecha_inscripcion DATE NOT NULL,
                    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE,
                    FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE
                )
            """)

            println("Tablas creadas/verificadas correctamente")
        } catch (e: Exception) {
            println("Error al inicializar BD: ${e.message}")
        }
    }

    fun closeConnection() {
        try {
            connection?.close()
            connection = null
        } catch (e: Exception) {
            println("Error al cerrar conexion: ${e.message}")
        }
    }
}