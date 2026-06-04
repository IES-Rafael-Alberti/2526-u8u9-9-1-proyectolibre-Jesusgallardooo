package service

// service/SocioService.kt

import exception.NotFoundException
import exception.ValidationException
import model.Socio
import repository.Repository
import validator.SocioValidator

class SocioService(private val socioRepository: Repository<Socio, Long>) {

    fun crearSocio(nombre: String, apellido: String, email: String, telefono: String): Socio {
        try {
            SocioValidator.validarSocioCompleto(nombre, apellido, email, telefono)
        } catch (e: ValidationException) {
            throw ValidationException("Error al crear socio: ${e.message}")
        }

        val socio = Socio(
            id = 0,
            nombre = nombre,
            apellido = apellido,
            email = email,
            telefono = telefono,
            activo = true
        )

        return socioRepository.create(socio)
    }

    fun obtenerSocio(id: Long): Socio {
        return socioRepository.findById(id)
            ?: throw NotFoundException("Socio con ID $id no encontrado")
    }

    fun listarTodosLosSocios(): List<Socio> {
        return socioRepository.findAll()
    }

    fun listarSociosActivos(): List<Socio> {
        return socioRepository.findAll().filter { it.activo }
    }

    fun listarSociosInactivos(): List<Socio> {
        return socioRepository.findAll().filter { !it.activo }
    }

    fun actualizarSocio(socio: Socio): Socio {
        obtenerSocio(socio.id)
        SocioValidator.validarSocioCompleto(
            socio.nombre, socio.apellido, socio.email, socio.telefono
        )
        return socioRepository.update(socio)
    }

    fun eliminarSocio(id: Long): Boolean {
        obtenerSocio(id)
        return socioRepository.delete(id)
    }

    fun darDeBaja(id: Long): Socio {
        val socio = obtenerSocio(id)
        val socioInactivo = socio.copy(activo = false)
        return socioRepository.update(socioInactivo)
    }

    fun darDeAlta(id: Long): Socio {
        val socio = obtenerSocio(id)
        val socioActivo = socio.copy(activo = true)
        return socioRepository.update(socioActivo)
    }

    fun contarSocios(): Int = socioRepository.findAll().size
    fun contarSociosActivos(): Int = listarSociosActivos().size
}