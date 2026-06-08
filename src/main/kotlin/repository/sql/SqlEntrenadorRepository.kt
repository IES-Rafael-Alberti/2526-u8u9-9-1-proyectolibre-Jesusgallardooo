package repository.sql

import model.Entrenador
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.Statement

class SqlEntrenadorRepository : Repository<Entrenador, Long> {

    private fun resultSetToEntrenador(rs: java.sql.ResultSet): Entrenador = Entrenador(
        id = rs.getLong("id"),
        nombre = rs.getString("nombre"),
        email = rs.getString("email"),
        especialidad = rs.getString("especialidad")
    )

    override fun findAll(): List<Entrenador> {
        val list = mutableListOf<Entrenador>()
        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM entrenadores ORDER BY id")
            while (rs.next()) list.add(resultSetToEntrenador(rs))
            rs.close(); stmt.close()
        } catch (e: Exception) { println("Error al listar entrenadores: ${e.message}") }
        return list
    }

    override fun findById(id: Long): Entrenador? {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("SELECT * FROM entrenadores WHERE id = ?")
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) { val e = resultSetToEntrenador(rs); rs.close(); pstmt.close(); return e }
            rs.close(); pstmt.close()
        } catch (e: Exception) { println("Error al buscar entrenador: ${e.message}") }
        return null
    }

    override fun create(entity: Entrenador): Entrenador {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("INSERT INTO entrenadores (nombre, email, especialidad) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
            pstmt.setString(1, entity.nombre); pstmt.setString(2, entity.email); pstmt.setString(3, entity.especialidad)
            pstmt.executeUpdate()
            val keys = pstmt.generatedKeys
            if (keys.next()) { val id = keys.getLong(1); pstmt.close(); return entity.copy(id = id) }
            pstmt.close()
        } catch (e: Exception) { println("Error al crear entrenador: ${e.message}") }
        return entity
    }

    override fun update(entity: Entrenador): Entrenador {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("UPDATE entrenadores SET nombre=?, email=?, especialidad=? WHERE id=?")
            pstmt.setString(1, entity.nombre); pstmt.setString(2, entity.email)
            pstmt.setString(3, entity.especialidad); pstmt.setLong(4, entity.id)
            val rows = pstmt.executeUpdate(); pstmt.close()
            if (rows > 0) return entity else throw NotFoundException("Entrenador con ID ${entity.id} no existe")
        } catch (e: Exception) { println("Error al actualizar entrenador: ${e.message}"); throw e }
    }

    override fun delete(id: Long): Boolean {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("DELETE FROM entrenadores WHERE id = ?")
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate(); pstmt.close(); return rows > 0
        } catch (e: Exception) { println("Error al eliminar entrenador: ${e.message}") }
        return false
    }
}
