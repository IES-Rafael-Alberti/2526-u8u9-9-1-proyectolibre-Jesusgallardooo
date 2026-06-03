package org.iesra.repository.sql

import org.iesra.model.Veterinario
import org.iesra.repository.interfaces.CrudRepository

class VeterinarioRepositorySql(private val manager: DataBaseManager) : CrudRepository<Veterinario, Int> {

    override fun findAll(): List<Veterinario> {
        val sql = "SELECT * FROM veterinario"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<Veterinario>()
                    while (rs.next()) {
                        results.add(
                            Veterinario(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                apellido = rs.getString("apellido"),
                                especialidad = rs.getString("especialidad"),
                                telefono = rs.getString("telefono")
                            )
                        )
                    }
                    results
                }
            }
        }
    }

    override fun findById(id: Int): Veterinario? {
        val sql = "SELECT * FROM veterinario WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        Veterinario(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            apellido = rs.getString("apellido"),
                            especialidad = rs.getString("especialidad"),
                            telefono = rs.getString("telefono")
                        )
                    } else null
                }
            }
        }
    }

    override fun save(entity: Veterinario): Boolean {
        val sql = "INSERT INTO veterinario (id, nombre, apellido, especialidad, telefono) VALUES (?, ?, ?, ?, ?)"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, entity.id)
                stmt.setString(2, entity.nombre)
                stmt.setString(3, entity.apellido)
                stmt.setString(4, entity.especialidad)
                stmt.setString(5, entity.telefono)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun update(entity: Veterinario): Boolean {
        val sql = "UPDATE veterinario SET nombre = ?, apellido = ?, especialidad = ?, telefono = ? WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.nombre)
                stmt.setString(2, entity.apellido)
                stmt.setString(3, entity.especialidad)
                stmt.setString(4, entity.telefono)
                stmt.setInt(5, entity.id)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun delete(id: Int): Boolean {
        val sql = "DELETE FROM veterinario WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate() > 0
            }
        }
    }
}
