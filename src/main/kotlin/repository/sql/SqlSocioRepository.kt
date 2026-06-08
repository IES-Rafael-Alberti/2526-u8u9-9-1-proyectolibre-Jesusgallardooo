package repository.sql

import model.Socio
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.ResultSet
import java.sql.Statement

/**
 * Repositorio de socios con persistencia en H2 mediante consultas SQL parametrizadas.
 */
class SqlSocioRepository : Repository<Socio, Long> {

    private fun resultSetToSocio(rs: ResultSet): Socio = Socio(
        id = rs.getLong("id"),
        nombre = rs.getString("nombre"),
        apellido = rs.getString("apellido"),
        email = rs.getString("email"),
        telefono = rs.getString("telefono"),
        activo = rs.getBoolean("activo")
    )

    override fun findAll(): List<Socio> {
        val socios = mutableListOf<Socio>()
        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM socios ORDER BY id")
            while (rs.next()) socios.add(resultSetToSocio(rs))
            rs.close(); stmt.close()
        } catch (e: Exception) { println("Error al listar socios: ${e.message}") }
        return socios
    }

    override fun findById(id: Long): Socio? {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("SELECT * FROM socios WHERE id = ?")
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) { val s = resultSetToSocio(rs); rs.close(); pstmt.close(); return s }
            rs.close(); pstmt.close()
        } catch (e: Exception) { println("Error al buscar socio: ${e.message}") }
        return null
    }

    override fun create(entity: Socio): Socio {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(
                "INSERT INTO socios (nombre, apellido, email, telefono, activo) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
            pstmt.setString(1, entity.nombre); pstmt.setString(2, entity.apellido)
            pstmt.setString(3, entity.email); pstmt.setString(4, entity.telefono); pstmt.setBoolean(5, entity.activo)
            pstmt.executeUpdate()
            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) { val id = generatedKeys.getLong(1); pstmt.close(); return entity.copy(id = id) }
            pstmt.close()
        } catch (e: Exception) { println("Error al crear socio: ${e.message}") }
        return entity
    }

    override fun update(entity: Socio): Socio {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("UPDATE socios SET nombre=?, apellido=?, email=?, telefono=?, activo=? WHERE id=?")
            pstmt.setString(1, entity.nombre); pstmt.setString(2, entity.apellido)
            pstmt.setString(3, entity.email); pstmt.setString(4, entity.telefono)
            pstmt.setBoolean(5, entity.activo); pstmt.setLong(6, entity.id)
            val rows = pstmt.executeUpdate(); pstmt.close()
            if (rows > 0) return entity else throw NotFoundException("Socio con ID ${entity.id} no existe")
        } catch (e: Exception) { println("Error al actualizar socio: ${e.message}"); throw e }
    }

    override fun delete(id: Long): Boolean {
        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement("DELETE FROM socios WHERE id = ?")
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate(); pstmt.close(); return rows > 0
        } catch (e: Exception) { println("Error al eliminar socio: ${e.message}") }
        return false
    }
}
