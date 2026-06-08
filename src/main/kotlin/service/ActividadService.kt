package service

import model.Actividad
import repository.Repository
import validator.ActividadValidator
import exception.NotFoundException

/**
 * Servicio que gestiona la lógica de negocio de las actividades.
 * Persiste simultáneamente en CSV y en H2.
 */
class ActividadService(
    private val csvRepo: Repository<Actividad, Long>,
    private val sqlRepo: Repository<Actividad, Long>
) {
    fun crearActividad(nombre: String, plazasMaximas: Int): Actividad {
        ActividadValidator.validarActividad(nombre, plazasMaximas)
        val actividad = Actividad(0, nombre, plazasMaximas)
        csvRepo.create(actividad)
        val sqlActividad = sqlRepo.create(actividad)
        println("  Guardado en CSV y H2 (ID: ${sqlActividad.id})")
        return sqlActividad
    }

    fun obtenerActividad(id: Long): Actividad =
        sqlRepo.findById(id) ?: csvRepo.findById(id) ?: throw NotFoundException("Actividad con ID $id no encontrada")

    fun listarTodasLasActividades(): List<Actividad> = csvRepo.findAll()

    fun actualizarActividad(actividad: Actividad): Actividad {
        ActividadValidator.validarActividad(actividad.nombre, actividad.plazasMaximas)
        csvRepo.update(actividad)
        val updated = sqlRepo.update(actividad)
        println("  Actualizado en CSV y H2")
        return updated
    }

    fun eliminarActividad(id: Long): Boolean {
        val csvOk = csvRepo.delete(id)
        val sqlOk = sqlRepo.delete(id)
        println("  Eliminado de CSV y H2")
        return csvOk && sqlOk
    }

    fun contarActividades(): Int = csvRepo.findAll().size
}