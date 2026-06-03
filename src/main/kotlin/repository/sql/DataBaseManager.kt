package org.iesra.repository.sql

import org.iesra.util.DataBaseConfig
import java.sql.Connection
import java.sql.DriverManager

class DataBaseManager(private val config: DataBaseConfig = DataBaseConfig()) {

    init {
        Class.forName("org.h2.Driver")
        createTables()
    }

    fun getConnection(): Connection =
        DriverManager.getConnection(config.url, config.user, config.password)

    private fun createTables() {
        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS propietario (
                        id INT PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        apellido VARCHAR(255) NOT NULL,
                        telefono VARCHAR(20) NOT NULL,
                        email VARCHAR(255) NOT NULL
                    )
                    """.trimIndent()
                )
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS mascota (
                        id INT PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        especie VARCHAR(100) NOT NULL,
                        raza VARCHAR(100) NOT NULL,
                        edad INT NOT NULL,
                        id_propietario INT NOT NULL,
                        FOREIGN KEY (id_propietario) REFERENCES propietario(id)
                    )
                    """.trimIndent()
                )
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS veterinario (
                        id INT PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        apellido VARCHAR(255) NOT NULL,
                        especialidad VARCHAR(255) NOT NULL,
                        telefono VARCHAR(20) NOT NULL
                    )
                    """.trimIndent()
                )
                stmt.executeUpdate(
                    """
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
                    """.trimIndent()
                )
            }
        }
    }
}
