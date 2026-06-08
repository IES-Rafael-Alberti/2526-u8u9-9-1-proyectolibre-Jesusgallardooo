// repository/sql/SqlSocioRepository.kt
package repository.sql

import model.Socio
import repository.Repository
import exception.NotFoundException
import util.DatabaseManager
import java.sql.ResultSet
import java.sql.Statement

class SqlSocioRepository : Repository<Socio, Long> {

    private fun resultSetToSocio(rs: ResultSet): Socio {
        return Socio(
            id = rs.getLong("id"),
            nombre = rs.getString("nombre"),
            apellido = rs.getString("apellido"),
            email = rs.getString("email"),
            telefono = rs.getString("telefono"),
            activo = rs.getBoolean("activo")
        )
    }

    override fun findAll(): List<Socio> {
        val socios = mutableListOf<Socio>()
        val sql = "SELECT * FROM socios ORDER BY id"

        try {
            val conn = DatabaseManager.getConnection()
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery(sql)

            while (rs.next()) {
                socios.add(resultSetToSocio(rs))
            }
            rs.close()
            stmt.close()
        } catch (e: Exception) {
            println("Error al listar socios: ${e.message}")
        }
        return socios
    }

    override fun findById(id: Long): Socio? {
        val sql = "SELECT * FROM socios WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                val socio = resultSetToSocio(rs)
                rs.close()
                pstmt.close()
                return socio
            }
            rs.close()
            pstmt.close()
        } catch (e: Exception) {
            println("Error al buscar socio: ${e.message}")
        }
        return null
    }

    override fun create(entity: Socio): Socio {
        val sql = "INSERT INTO socios (nombre, apellido, email, telefono, activo) VALUES (?, ?, ?, ?, ?)"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            pstmt.setString(1, entity.nombre)
            pstmt.setString(2, entity.apellido)
            pstmt.setString(3, entity.email)
            pstmt.setString(4, entity.telefono)
            pstmt.setBoolean(5, entity.activo)

            pstmt.executeUpdate()
            val generatedKeys = pstmt.generatedKeys

            if (generatedKeys.next()) {
                val id = generatedKeys.getLong(1)
                pstmt.close()
                return entity.copy(id = id)
            }
            pstmt.close()
        } catch (e: Exception) {
            println("Error al crear socio: ${e.message}")
        }
        return entity
    }

    override fun update(entity: Socio): Socio {
        val sql = "UPDATE socios SET nombre = ?, apellido = ?, email = ?, telefono = ?, activo = ? WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, entity.nombre)
            pstmt.setString(2, entity.apellido)
            pstmt.setString(3, entity.email)
            pstmt.setString(4, entity.telefono)
            pstmt.setBoolean(5, entity.activo)
            pstmt.setLong(6, entity.id)

            val rows = pstmt.executeUpdate()
            pstmt.close()

            if (rows > 0) {
                return entity
            } else {
                throw NotFoundException("Socio con ID ${entity.id} no existe")
            }
        } catch (e: Exception) {
            println("Error al actualizar socio: ${e.message}")
            throw e
        }
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM socios WHERE id = ?"

        try {
            val conn = DatabaseManager.getConnection()
            val pstmt = conn.prepareStatement(sql)
            pstmt.setLong(1, id)
            val rows = pstmt.executeUpdate()
            pstmt.close()
            return rows > 0
        } catch (e: Exception) {
            println("Error al eliminar socio: ${e.message}")
        }
        return false
    }
}