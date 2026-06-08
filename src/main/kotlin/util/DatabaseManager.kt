package util

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

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS actividades (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    plazas_maximas INT NOT NULL
                )
            """)

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS entrenadores (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    especialidad VARCHAR(50) NOT NULL
                )
            """)

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

    // NUEVO MÉTODO: Vaciar todas las tablas
    fun clearAllTables() {
        try {
            val conn = getConnection()

            // Desactivar restricciones de claves foráneas
            conn.createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE")

            val tables = listOf("inscripciones", "socios", "actividades", "entrenadores")

            for (table in tables) {
                conn.createStatement().execute("DELETE FROM $table")
                println("   Tabla '$table' vaciada")
            }

            // Restaurar restricciones
            conn.createStatement().execute("SET REFERENTIAL_INTEGRITY TRUE")

            // Reiniciar contadores
            conn.createStatement().execute("ALTER TABLE socios ALTER COLUMN id RESTART WITH 1")
            conn.createStatement().execute("ALTER TABLE actividades ALTER COLUMN id RESTART WITH 1")
            conn.createStatement().execute("ALTER TABLE entrenadores ALTER COLUMN id RESTART WITH 1")
            conn.createStatement().execute("ALTER TABLE inscripciones ALTER COLUMN id RESTART WITH 1")

            println("Todas las tablas han sido vaciadas correctamente")
        } catch (e: Exception) {
            println("Error al vaciar tablas: ${e.message}")
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