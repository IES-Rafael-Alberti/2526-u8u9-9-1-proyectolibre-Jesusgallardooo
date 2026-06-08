package repository.sql

import model.Actividad
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.Statement

class SqlActividadRepository : Repository<Actividad, Long> {

    private fun resultSetToActividad(rs: java.sql.ResultSet): Actividad = Actividad(
        id = rs.getLong("id"),
        nombre = rs.getString("nombre"),
        plazasMaximas = rs.getInt("plazas_maximas")
    )

    override fun findAll(): List<Actividad> {
        val list = mutableListOf<Actividad>()
        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM actividades ORDER BY id")
            while (rs.next()) list.add(resultSetToActividad(rs))
            rs.close(); stmt.close()
        } catch (e: Exception) { println("Error al listar actividades: ${e.message}") }
        return list
    }

    override fun findById(id: Long): Actividad? {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("SELECT * FROM actividades WHERE id = ?")
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) { val a = resultSetToActividad(rs); rs.close(); pstmt.close(); return a }
            rs.close(); pstmt.close()
        } catch (e: Exception) { println("Error al buscar actividad: ${e.message}") }
        return null
    }

    override fun create(entity: Actividad): Actividad {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("INSERT INTO actividades (nombre, plazas_maximas) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            pstmt.setString(1, entity.nombre); pstmt.setInt(2, entity.plazasMaximas)
            pstmt.executeUpdate()
            val keys = pstmt.generatedKeys
            if (keys.next()) { val id = keys.getLong(1); pstmt.close(); return entity.copy(id = id) }
            pstmt.close()
        } catch (e: Exception) { println("Error al crear actividad: ${e.message}") }
        return entity
    }

    override fun update(entity: Actividad): Actividad {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("UPDATE actividades SET nombre=?, plazas_maximas=? WHERE id=?")
            pstmt.setString(1, entity.nombre); pstmt.setInt(2, entity.plazasMaximas); pstmt.setLong(3, entity.id)
            val rows = pstmt.executeUpdate(); pstmt.close()
            if (rows > 0) return entity else throw NotFoundException("Actividad con ID ${entity.id} no existe")
        } catch (e: Exception) { println("Error al actualizar actividad: ${e.message}"); throw e }
    }

    override fun delete(id: Long): Boolean {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("DELETE FROM actividades WHERE id = ?")
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate(); pstmt.close(); return rows > 0
        } catch (e: Exception) { println("Error al eliminar actividad: ${e.message}") }
        return false
    }
}
