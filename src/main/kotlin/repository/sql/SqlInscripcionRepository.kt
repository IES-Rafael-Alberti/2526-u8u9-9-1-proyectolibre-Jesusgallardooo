// repository/sql/SqlInscripcionRepository.kt
package repository.sql

import model.Inscripcion
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.Date
import java.sql.Statement

class SqlInscripcionRepository : Repository<Inscripcion, Long> {

    private fun resultSetToInscripcion(rs: java.sql.ResultSet): Inscripcion {
        return Inscripcion(
            id = rs.getLong("id"),
            socioId = rs.getLong("socio_id"),
            actividadId = rs.getLong("actividad_id"),
            fechaInscripcion = rs.getDate("fecha_inscripcion").toLocalDate()
        )
    }

    override fun findAll(): List<Inscripcion> {
        val inscripciones = mutableListOf<Inscripcion>()
        val sql = "SELECT * FROM inscripciones ORDER BY id"

        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery(sql)

            while (rs.next()) {
                inscripciones.add(resultSetToInscripcion(rs))
            }
            rs.close()
            stmt.close()
        } catch (e: Exception) {
            println("Error al listar inscripciones: ${e.message}")
        }
        return inscripciones
    }

    override fun findById(id: Long): Inscripcion? {
        val sql = "SELECT * FROM inscripciones WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                val inscripcion = resultSetToInscripcion(rs)
                rs.close()
                pstmt.close()
                return inscripcion
            }
            rs.close()
            pstmt.close()
        } catch (e: Exception) {
            println("Error al buscar inscripcion: ${e.message}")
        }
        return null
    }

    override fun create(entity: Inscripcion): Inscripcion {
        val sql = "INSERT INTO inscripciones (socio_id, actividad_id, fecha_inscripcion) VALUES (?, ?, ?)"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            pstmt.setLong(1, entity.socioId)
            pstmt.setLong(2, entity.actividadId)
            pstmt.setDate(3, Date.valueOf(entity.fechaInscripcion))

            pstmt.executeUpdate()
            val generatedKeys = pstmt.generatedKeys

            if (generatedKeys.next()) {
                val id = generatedKeys.getLong(1)
                pstmt.close()
                return entity.copy(id = id)
            }
            pstmt.close()
        } catch (e: Exception) {
            println("Error al crear inscripcion: ${e.message}")
        }
        return entity
    }

    override fun update(entity: Inscripcion): Inscripcion {
        val sql = "UPDATE inscripciones SET socio_id = ?, actividad_id = ?, fecha_inscripcion = ? WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, entity.socioId)
            pstmt.setLong(2, entity.actividadId)
            pstmt.setDate(3, Date.valueOf(entity.fechaInscripcion))
            pstmt.setLong(4, entity.id)

            val rows = pstmt.executeUpdate()
            pstmt.close()

            if (rows > 0) {
                return entity
            } else {
                throw NotFoundException("Inscripcion con ID ${entity.id} no existe")
            }
        } catch (e: Exception) {
            println("Error al actualizar inscripcion: ${e.message}")
            throw e
        }
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM inscripciones WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate()
            pstmt.close()
            return rows > 0
        } catch (e: Exception) {
            println("Error al eliminar inscripcion: ${e.message}")
        }
        return false
    }
}