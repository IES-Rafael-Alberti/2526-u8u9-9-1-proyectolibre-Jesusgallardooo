package service

// service/InscripcionService.kt

import model.Inscripcion
import model.Socio
import model.Actividad
import repository.Repository
import validator.InscripcionValidator
import exception.*
import java.time.LocalDate

class InscripcionService(
    private val inscripcionRepository: Repository<Inscripcion, Long>,
    private val socioRepository: Repository<Socio, Long>,
    private val actividadRepository: Repository<Actividad, Long>
) {

    fun inscribirSocio(socioId: Long, actividadId: Long, fechaInscripcion: LocalDate): Inscripcion {
        // Verificar que existen
        val socio = socioRepository.findById(socioId)
            ?: throw SocioNotFoundException("Socio con id $socioId no encontrado")

        val actividad = actividadRepository.findById(actividadId)
            ?: throw ActividadNotFoundException(actividadId)

        // Validar que el socio esté activo
        if (!socio.activo) {
            throw SocioInactivoException(socioId)
        }

        // Validar fecha
        try {
            InscripcionValidator.validarInscripcion(socioId, actividadId, fechaInscripcion)
        } catch (e: ValidationException) {
            throw ValidationException("Error en inscripción: ${e.message}")
        }

        // Verificar plazas disponibles
        val inscripcionesActuales = inscripcionRepository.findAll()
            .filter { it.actividadId == actividadId }

        if (inscripcionesActuales.size >= actividad.plazasMaximas) {
            throw ActividadSinPlazasException(actividadId, actividad.plazasMaximas - inscripcionesActuales.size)
        }

        // Verificar que no esté ya inscrito
        val yaInscrito = inscripcionesActuales.any { it.socioId == socioId }
        if (yaInscrito) {
            throw SocioYaInscritoException(socioId, actividadId)
        }

        val inscripcion = Inscripcion(
            id = 0,
            socioId = socioId,
            actividadId = actividadId,
            fechaInscricao = fechaInscripcion
        )

        return inscripcionRepository.create(inscripcion)
    }

    fun obtenerInscripcion(id: Long): Inscripcion {
        return inscripcionRepository.findById(id)
            ?: throw InscripcionNotFoundException(id)
    }

    fun listarTodasLasInscripciones(): List<Inscripcion> {
        return inscripcionRepository.findAll()
    }

    fun listarInscripcionesPorSocio(socioId: Long): List<Inscripcion> {
        return inscripcionRepository.findAll().filter { it.socioId == socioId }
    }

    fun listarInscripcionesPorActividad(actividadId: Long): List<Inscripcion> {
        return inscripcionRepository.findAll().filter { it.actividadId == actividadId }
    }

    fun obtenerPlazasDisponibles(actividadId: Long): Int {
        val actividad = actividadRepository.findById(actividadId)
            ?: throw ActividadNotFoundException(actividadId)

        val inscripciones = listarInscripcionesPorActividad(actividadId)
        return actividad.plazasMaximas - inscripciones.size
    }

    fun cancelarInscripcion(id: Long): Boolean {
        obtenerInscripcion(id)
        return inscripcionRepository.delete(id)
    }

    fun cancelarInscripcionesPorSocio(socioId: Long): Int {
        val inscripciones = listarInscripcionesPorSocio(socioId)
        var eliminadas = 0
        inscripciones.forEach {
            if (inscripcionRepository.delete(it.id)) eliminadas++
        }
        return eliminadas
    }
}