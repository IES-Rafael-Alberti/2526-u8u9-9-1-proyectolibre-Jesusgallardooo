package org.iesra.repository.sql

import org.iesra.model.Propietario
import org.iesra.repository.interfaces.CrudRepository

class PropietarioRepositorySql(private val manager: DataBaseManager) : CrudRepository<Propietario, Int> {

    override fun findAll(): List<Propietario> {
        val sql = "SELECT * FROM propietario"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<Propietario>()
                    while (rs.next()) {
                        results.add(
                            Propietario(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                apellido = rs.getString("apellido"),
                                telefono = rs.getString("telefono"),
                                email = rs.getString("email")
                            )
                        )
                    }
                    results
                }
            }
        }
    }

    override fun findById(id: Int): Propietario? {
        val sql = "SELECT * FROM propietario WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        Propietario(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            apellido = rs.getString("apellido"),
                            telefono = rs.getString("telefono"),
                            email = rs.getString("email")
                        )
                    } else null
                }
            }
        }
    }

    override fun save(entity: Propietario): Boolean {
        val sql = "INSERT INTO propietario (id, nombre, apellido, telefono, email) VALUES (?, ?, ?, ?, ?)"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, entity.id)
                stmt.setString(2, entity.nombre)
                stmt.setString(3, entity.apellido)
                stmt.setString(4, entity.telefono)
                stmt.setString(5, entity.email)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun update(entity: Propietario): Boolean {
        val sql = "UPDATE propietario SET nombre = ?, apellido = ?, telefono = ?, email = ? WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.nombre)
                stmt.setString(2, entity.apellido)
                stmt.setString(3, entity.telefono)
                stmt.setString(4, entity.email)
                stmt.setInt(5, entity.id)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun delete(id: Int): Boolean {
        val sql = "DELETE FROM propietario WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate() > 0
            }
        }
    }
}
