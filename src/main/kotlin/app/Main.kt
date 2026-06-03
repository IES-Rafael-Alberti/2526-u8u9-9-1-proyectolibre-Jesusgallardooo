package app

import java.sql.DriverManager

fun main() {
    println("=== VERIFICACION CONEXION H2 - Inserts de prueba ===\n")

    Class.forName("org.h2.Driver")

    val userDir = System.getProperty("user.dir")
    println("Directorio de trabajo (user.dir): $userDir")
    println("(esperado: /home/jesus/Documents/workspace/prog/kotlin/proyecto_final/2526-u8u9-9-1-proyectolibre-Jesusgallardooo)\n")

    val url = "jdbc:h2:./data/vet_manager"
    val absoluteUrl = "jdbc:h2:$userDir/data/vet_manager"
    val user = "jesus"
    val password = "jesus"

    println("Conectando a (relativa): $url")
    println("Equivalente absoluta: $absoluteUrl")
    println("Usuario: $user")

    val conn = DriverManager.getConnection(url, user, password)

    conn.use { connection ->

        // Crear tablas si no existen (para que funcione en BD vacia)
        connection.createStatement().use { stmt ->
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS propietario (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    apellido VARCHAR(255) NOT NULL,
                    telefono VARCHAR(20) NOT NULL,
                    email VARCHAR(255) NOT NULL
                )
            """.trimIndent())
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS veterinario (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    apellido VARCHAR(255) NOT NULL,
                    especialidad VARCHAR(255) NOT NULL,
                    telefono VARCHAR(20) NOT NULL
                )
            """.trimIndent())
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS mascota (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    especie VARCHAR(100) NOT NULL,
                    raza VARCHAR(100) NOT NULL,
                    edad INT NOT NULL,
                    id_propietario INT NOT NULL,
                    FOREIGN KEY (id_propietario) REFERENCES propietario(id)
                )
            """.trimIndent())
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cita (
                    id INT PRIMARY KEY,
                    id_mascota INT NOT NULL,
                    id_veterinario INT NOT NULL,
                    fecha DATE NOT NULL,
                    hora TIME NOT NULL,
                    motivo VARCHAR(500) NOT NULL,
                    FOREIGN KEY (id_mascota) REFERENCES mascota(id),
                    FOREIGN KEY (id_veterinario) REFERENCES veterinario(id)
                )
            """.trimIndent())
            println("Tablas creadas/verificadas.\n")
        }

        // --- INSERTS (MERGE para ser re-ejecutable) ---
        connection.createStatement().use { stmt ->
            stmt.executeUpdate("""
                MERGE INTO propietario (id, nombre, apellido, telefono, email) KEY (id) VALUES
                (1, 'Juan', 'Perez', '600111222', 'juan@email.com'),
                (2, 'Maria', 'Lopez', '600333444', 'maria@email.com'),
                (3, 'Carlos', 'Garcia', '600555666', 'carlos@email.com')
            """.trimIndent())
            println("[INSERT] 3 propietarios: Juan, Maria, Carlos")

            stmt.executeUpdate("""
                MERGE INTO veterinario (id, nombre, apellido, especialidad, telefono) KEY (id) VALUES
                (1, 'Dr. Pedro', 'Ruiz', 'Cirugia', '600777888'),
                (2, 'Dra. Ana', 'Gil', 'Dermatologia', '600999000')
            """.trimIndent())
            println("[INSERT] 2 veterinarios: Dr. Pedro, Dra. Ana")

            stmt.executeUpdate("""
                MERGE INTO mascota (id, nombre, especie, raza, edad, id_propietario) KEY (id) VALUES
                (1, 'Rex', 'Perro', 'Pastor Aleman', 3, 1),
                (2, 'Misu', 'Gato', 'Persa', 2, 1),
                (3, 'Luna', 'Perro', 'Labrador', 1, 2)
            """.trimIndent())
            println("[INSERT] 3 mascotas: Rex, Misu, Luna")

            stmt.executeUpdate("""
                MERGE INTO cita (id, id_mascota, id_veterinario, fecha, hora, motivo) KEY (id) VALUES
                (1, 1, 1, CURRENT_DATE, CURRENT_TIME, 'Revision general'),
                (2, 2, 2, CURRENT_DATE, CURRENT_TIME, 'Problema dermatologico')
            """.trimIndent())
            println("[INSERT] 2 citas: Revision general, Problema dermatologico\n")
        }

        // --- SELECTS ---
        fun mostrarTabla(nombre: String, sql: String) {
            println("=== $nombre ===")
            connection.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                val meta = rs.metaData
                val cols = (1..meta.columnCount).map { meta.getColumnName(it) }
                println("  Columnas: $cols")
                var count = 0
                while (rs.next()) {
                    val valores = cols.map { rs.getString(it) }
                    println("  Fila ${++count}: $valores")
                }
                if (count == 0) println("  (sin datos)")
                println()
            }
        }

        mostrarTabla("PROPIETARIO", "SELECT * FROM propietario")
        mostrarTabla("VETERINARIO", "SELECT * FROM veterinario")
        mostrarTabla("MASCOTA", "SELECT * FROM mascota")
        mostrarTabla("CITA", "SELECT * FROM cita")
    }

    println("=== FIN: Verificacion completada ===")
}
