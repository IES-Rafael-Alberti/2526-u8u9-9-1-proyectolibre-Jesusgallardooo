// service/SocioService.kt
package service

import model.Socio
import repository.Repository
import validator.SocioValidator
import exception.NotFoundException

class SocioService(
    private val csvRepo: Repository<Socio, Long>,
    private val sqlRepo: Repository<Socio, Long>
) {
    fun crearSocio(nombre: String, apellido: String, email: String, telefono: String): Socio {
        SocioValidator.validarSocioCompleto(nombre, apellido, email, telefono)
        val socio = Socio(0, nombre, apellido, email, telefono, true)
        csvRepo.create(socio)
        val sqlSocio = sqlRepo.create(socio)
        println("  Guardado en CSV y H2 (ID: ${sqlSocio.id})")
        return sqlSocio
    }

    fun obtenerSocio(id: Long): Socio =
        sqlRepo.findById(id) ?: csvRepo.findById(id) ?: throw NotFoundException("Socio con ID $id no encontrado")

    fun listarTodosLosSocios(): List<Socio> = csvRepo.findAll()

    fun listarSociosActivos(): List<Socio> = csvRepo.findAll().filter { it.activo }

    fun listarSociosInactivos(): List<Socio> = csvRepo.findAll().filter { !it.activo }

    fun actualizarSocio(socio: Socio): Socio {
        SocioValidator.validarSocioCompleto(socio.nombre, socio.apellido, socio.email, socio.telefono)
        csvRepo.update(socio)
        val updated = sqlRepo.update(socio)
        println("  Actualizado en CSV y H2")
        return updated
    }

    fun eliminarSocio(id: Long): Boolean {
        val csvOk = csvRepo.delete(id)
        val sqlOk = sqlRepo.delete(id)
        println("  Eliminado de CSV y H2")
        return csvOk && sqlOk
    }

    fun darDeBaja(id: Long): Socio = actualizarSocio(obtenerSocio(id).copy(activo = false))

    fun darDeAlta(id: Long): Socio = actualizarSocio(obtenerSocio(id).copy(activo = true))

    fun contarSocios(): Int = csvRepo.findAll().size

    fun contarSociosActivos(): Int = listarSociosActivos().size
}