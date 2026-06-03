package org.iesra.repository.sql

import org.iesra.model.Cita
import org.iesra.repository.interfaces.CrudRepository
import java.sql.Date
import java.sql.Time

class CitaRepositorySql(private val manager: DataBaseManager) : CrudRepository<Cita, Int> {

    override fun findAll(): List<Cita> {
        val sql = "SELECT * FROM cita"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    val results = mutableListOf<Cita>()
                    while (rs.next()) {
                        results.add(
                            Cita(
                                id = rs.getInt("id"),
                                idMascota = rs.getInt("id_mascota"),
                                idVeterinario = rs.getInt("id_veterinario"),
                                fecha = rs.getDate("fecha").toLocalDate(),
                                hora = rs.getTime("hora").toLocalTime(),
                                motivo = rs.getString("motivo")
                            )
                        )
                    }
                    results
                }
            }
        }
    }

    override fun findById(id: Int): Cita? {
        val sql = "SELECT * FROM cita WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        Cita(
                            id = rs.getInt("id"),
                            idMascota = rs.getInt("id_mascota"),
                            idVeterinario = rs.getInt("id_veterinario"),
                            fecha = rs.getDate("fecha").toLocalDate(),
                            hora = rs.getTime("hora").toLocalTime(),
                            motivo = rs.getString("motivo")
                        )
                    } else null
                }
            }
        }
    }

    override fun save(entity: Cita): Boolean {
        val sql = "INSERT INTO cita (id, id_mascota, id_veterinario, fecha, hora, motivo) VALUES (?, ?, ?, ?, ?, ?)"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, entity.id)
                stmt.setInt(2, entity.idMascota)
                stmt.setInt(3, entity.idVeterinario)
                stmt.setDate(4, Date.valueOf(entity.fecha))
                stmt.setTime(5, Time.valueOf(entity.hora))
                stmt.setString(6, entity.motivo)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun update(entity: Cita): Boolean {
        val sql = "UPDATE cita SET id_mascota = ?, id_veterinario = ?, fecha = ?, hora = ?, motivo = ? WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, entity.idMascota)
                stmt.setInt(2, entity.idVeterinario)
                stmt.setDate(3, Date.valueOf(entity.fecha))
                stmt.setTime(4, Time.valueOf(entity.hora))
                stmt.setString(5, entity.motivo)
                stmt.setInt(6, entity.id)
                stmt.executeUpdate() > 0
            }
        }
    }

    override fun delete(id: Int): Boolean {
        val sql = "DELETE FROM cita WHERE id = ?"
        val conn = manager.getConnection()
        return conn.use { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate() > 0
            }
        }
    }
}
