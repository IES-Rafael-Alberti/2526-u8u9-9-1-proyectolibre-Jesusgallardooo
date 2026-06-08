// repository/sql/SqlActividadRepository.kt
package repository.sql

import model.Actividad
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.Statement

class SqlActividadRepository : Repository<Actividad, Long> {

    private fun resultSetToActividad(rs: java.sql.ResultSet): Actividad {
        return Actividad(
            id = rs.getLong("id"),
            nombre = rs.getString("nombre"),
            plazasMaximas = rs.getInt("plazas_maximas")
        )
    }

    override fun findAll(): List<Actividad> {
        val actividades = mutableListOf<Actividad>()
        val sql = "SELECT * FROM actividades ORDER BY id"

        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery(sql)

            while (rs.next()) {
                actividades.add(resultSetToActividad(rs))
            }
            rs.close()
            stmt.close()
        } catch (e: Exception) {
            println("Error al listar actividades: ${e.message}")
        }
        return actividades
    }

    override fun findById(id: Long): Actividad? {
        val sql = "SELECT * FROM actividades WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                val actividad = resultSetToActividad(rs)
                rs.close()
                pstmt.close()
                return actividad
            }
            rs.close()
            pstmt.close()
        } catch (e: Exception) {
            println("Error al buscar actividad: ${e.message}")
        }
        return null
    }

    override fun create(entity: Actividad): Actividad {
        val sql = "INSERT INTO actividades (nombre, plazas_maximas) VALUES (?, ?)"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            pstmt.setString(1, entity.nombre)
            pstmt.setInt(2, entity.plazasMaximas)

            pstmt.executeUpdate()
            val generatedKeys = pstmt.generatedKeys

            if (generatedKeys.next()) {
                val id = generatedKeys.getLong(1)
                pstmt.close()
                return entity.copy(id = id)
            }
            pstmt.close()
        } catch (e: Exception) {
            println("Error al crear actividad: ${e.message}")
        }
        return entity
    }

    override fun update(entity: Actividad): Actividad {
        val sql = "UPDATE actividades SET nombre = ?, plazas_maximas = ? WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, entity.nombre)
            pstmt.setInt(2, entity.plazasMaximas)
            pstmt.setLong(3, entity.id)

            val rows = pstmt.executeUpdate()
            pstmt.close()

            if (rows > 0) {
                return entity
            } else {
                throw NotFoundException("Actividad con ID ${entity.id} no existe")
            }
        } catch (e: Exception) {
            println("Error al actualizar actividad: ${e.message}")
            throw e
        }
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM actividades WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate()
            pstmt.close()
            return rows > 0
        } catch (e: Exception) {
            println("Error al eliminar actividad: ${e.message}")
        }
        return false
    }
}