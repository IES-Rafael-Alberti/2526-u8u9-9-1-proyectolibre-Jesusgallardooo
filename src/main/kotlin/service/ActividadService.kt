package service
// service/ActividadService.kt

import model.Actividad
import repository.Repository
import validator.ActividadValidator

class ActividadService(private val actividadRepository: Repository<Actividad, Long>) {

    fun crearActividad(nombre: String, plazasMaximas: Int): Actividad {
        try {
            ActividadValidator.validarActividad(nombre, plazasMaximas)
        } catch (e: ValidationException) {
            throw ValidationException("Error al crear actividad: ${e.message}")
        }

        val actividad = Actividad(
            id = 0,
            nombre = nombre,
            plazasMaximas = plazasMaximas
        )

        return actividadRepository.create(actividad)
    }

    fun obtenerActividad(id: Long): Actividad {
        return actividadRepository.findById(id)
            ?: throw NotFoundException("Actividad con ID $id no encontrada")
    }

    fun listarTodasLasActividades(): List<Actividad> {
        return actividadRepository.findAll()
    }

    fun actualizarActividad(actividad: Actividad): Actividad {
        obtenerActividad(actividad.id)
        ActividadValidator.validarActividad(actividad.nombre, actividad.plazasMaximas)
        return actividadRepository.update(actividad)
    }

    fun eliminarActividad(id: Long): Boolean {
        obtenerActividad(id)
        return actividadRepository.delete(id)
    }

    fun contarActividades(): Int = actividadRepository.findAll().size
}