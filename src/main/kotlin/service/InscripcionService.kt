// service/InscripcionService.kt
package service

import model.Inscripcion
import model.Socio
import model.Actividad
import repository.Repository
import validator.InscripcionValidator
import exception.*
import java.time.LocalDate

class InscripcionService(
    private val csvRepo: Repository<Inscripcion, Long>,
    private val sqlRepo: Repository<Inscripcion, Long>,
    private val socioRepo: Repository<Socio, Long>,
    private val actividadRepo: Repository<Actividad, Long>
) {
    fun inscribirSocio(socioId: Long, actividadId: Long, fechaInscripcion: LocalDate): Inscripcion {
        val socio = socioRepo.findById(socioId) ?: throw SocioNotFoundException("Socio con ID $socioId no encontrado")
        val actividad = actividadRepo.findById(actividadId) ?: throw ActividadNotFoundException(actividadId)
        if (!socio.activo) throw SocioInactivoException(socioId)

        InscripcionValidator.validarInscripcion(socioId, actividadId, fechaInscripcion)

        val inscripcionesActuales = sqlRepo.findAll().filter { it.actividadId == actividadId }
        if (inscripcionesActuales.size >= actividad.plazasMaximas)
            throw ActividadSinPlazasException(actividadId, actividad.plazasMaximas - inscripcionesActuales.size)
        if (inscripcionesActuales.any { it.socioId == socioId })
            throw SocioYaInscritoException(socioId, actividadId)

        val inscripcion = Inscripcion(0, socioId, actividadId, fechaInscripcion)
        csvRepo.create(inscripcion)
        val sqlInscripcion = sqlRepo.create(inscripcion)
        println("  Guardado en CSV y H2 (ID: ${sqlInscripcion.id})")
        return sqlInscripcion
    }

    fun listarTodasLasInscripciones(): List<Inscripcion> = csvRepo.findAll()

    fun listarInscripcionesPorSocio(socioId: Long): List<Inscripcion> =
        csvRepo.findAll().filter { it.socioId == socioId }

    fun listarInscripcionesPorActividad(actividadId: Long): List<Inscripcion> =
        csvRepo.findAll().filter { it.actividadId == actividadId }

    fun obtenerPlazasDisponibles(actividadId: Long): Int {
        val actividad = actividadRepo.findById(actividadId) ?: throw ActividadNotFoundException(actividadId)
        val inscripciones = listarInscripcionesPorActividad(actividadId)
        return actividad.plazasMaximas - inscripciones.size
    }

    fun cancelarInscripcion(id: Long): Boolean {
        val csvOk = csvRepo.delete(id)
        val sqlOk = sqlRepo.delete(id)
        println("  Cancelado de CSV y H2")
        return csvOk && sqlOk
    }
}