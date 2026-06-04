// repository/sql/SqlEntrenadorRepository.kt
package repository.sql

import model.Entrenador
import repository.Repository
import exception.NotFoundException
import java.sql.Statement

class SqlEntrenadorRepository : Repository<Entrenador, Long> {

    private fun resultSetToEntrenador(rs: java.sql.ResultSet): Entrenador {
        return Entrenador(
            id = rs.getLong("id"),
            nombre = rs.getString("nombre"),
            email = rs.getString("email"),
            especialidad = rs.getString("especialidad")
        )
    }

    override fun findAll(): List<Entrenador> {
        val entrenadores = mutableListOf<Entrenador>()
        val sql = "SELECT * FROM entrenadores ORDER BY id"

        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery(sql)

            while (rs.next()) {
                entrenadores.add(resultSetToEntrenador(rs))
            }
            rs.close()
            stmt.close()
        } catch (e: Exception) {
            println("Error al listar entrenadores: ${e.message}")
        }
        return entrenadores
    }

    override fun findById(id: Long): Entrenador? {
        val sql = "SELECT * FROM entrenadores WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                val entrenador = resultSetToEntrenador(rs)
                rs.close()
                pstmt.close()
                return entrenador
            }
            rs.close()
            pstmt.close()
        } catch (e: Exception) {
            println("Error al buscar entrenador: ${e.message}")
        }
        return null
    }

    override fun create(entity: Entrenador): Entrenador {
        val sql = "INSERT INTO entrenadores (nombre, email, especialidad) VALUES (?, ?, ?)"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            pstmt.setString(1, entity.nombre)
            pstmt.setString(2, entity.email)
            pstmt.setString(3, entity.especialidad)

            pstmt.executeUpdate()
            val generatedKeys = pstmt.generatedKeys

            if (generatedKeys.next()) {
                val id = generatedKeys.getLong(1)
                pstmt.close()
                return entity.copy(id = id)
            }
            pstmt.close()
        } catch (e: Exception) {
            println("Error al crear entrenador: ${e.message}")
        }
        return entity
    }

    override fun update(entity: Entrenador): Entrenador {
        val sql = "UPDATE entrenadores SET nombre = ?, email = ?, especialidad = ? WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, entity.nombre)
            pstmt.setString(2, entity.email)
            pstmt.setString(3, entity.especialidad)
            pstmt.setLong(4, entity.id)

            val rows = pstmt.executeUpdate()
            pstmt.close()

            if (rows > 0) {
                return entity
            } else {
                throw NotFoundException("Entrenador con ID ${entity.id} no existe")
            }
        } catch (e: Exception) {
            println("Error al actualizar entrenador: ${e.message}")
            throw e
        }
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM entrenadores WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate()
            pstmt.close()
            return rows > 0
        } catch (e: Exception) {
            println("Error al eliminar entrenador: ${e.message}")
        }
        return false
    }
}