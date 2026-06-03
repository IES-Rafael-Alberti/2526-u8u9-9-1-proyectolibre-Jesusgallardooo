package org.iesra.repository.sql

import org.iesra.util.DataBaseConfig
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager

class DataBaseManager(private val config: DataBaseConfig = DataBaseConfig.fromEnv()) {

    private val log = LoggerFactory.getLogger(DataBaseManager::class.java)

    init {
        Class.forName("org.h2.Driver")
        log.info("Inicializando H2...")
        log.info("URL JDBC: {}", config.url)
        log.info("Modo H2: {}", if (config.url.contains(":mem:")) "MEMORIA (volatil)" else "ARCHIVO (persistente)")
        log.info("Usuario H2: {}", config.user)
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
