package org.iesra.repository.sql

import org.iesra.model.Mascota
import org.iesra.repository.interfaces.CrudRepository

class MascotaRepositorySql(private val manager: DataBaseManager) : CrudRepository<Mascota, Int> {

    override fun findAll(): List<Mascota> {
        val sql = "SELECT * FROM mascota"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<Mascota>()
                    while (rs.next()) {
                        results.add(
                            Mascota(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                especie = rs.getString("especie"),
                                raza = rs.getString("raza"),
                                edad = rs.getInt("edad"),
                                idPropietario = rs.getInt("id_propietario")
                            )
                        )
                    }
                    results
                }
            }
        }
    }

    override fun findById(id: Int): Mascota? {
        val sql = "SELECT * FROM mascota WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        Mascota(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            especie = rs.getString("especie"),
                            raza = rs.getString("raza"),
                            edad = rs.getInt("edad"),
                            idPropietario = rs.getInt("id_propietario")
                        )
                    } else null
                }
            }
        }
    }

    override fun save(entity: Mascota): Boolean {
        val sql = "INSERT INTO mascota (id, nombre, especie, raza, edad, id_propietario) VALUES (?, ?, ?, ?, ?, ?)"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, entity.id)
                stmt.setString(2, entity.nombre)
                stmt.setString(3, entity.especie)
                stmt.setString(4, entity.raza)
                stmt.setInt(5, entity.edad)
                stmt.setInt(6, entity.idPropietario)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun update(entity: Mascota): Boolean {
        val sql = "UPDATE mascota SET nombre = ?, especie = ?, raza = ?, edad = ?, id_propietario = ? WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.nombre)
                stmt.setString(2, entity.especie)
                stmt.setString(3, entity.raza)
                stmt.setInt(4, entity.edad)
                stmt.setInt(5, entity.idPropietario)
                stmt.setInt(6, entity.id)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun delete(id: Int): Boolean {
        val sql = "DELETE FROM mascota WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate() > 0
            }
        }
    }
}
