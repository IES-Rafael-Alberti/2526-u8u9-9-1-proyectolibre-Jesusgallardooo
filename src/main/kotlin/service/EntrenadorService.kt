package service

// service/EntrenadorService.kt

import model.Entrenador
import repository.Repository
import validator.EntrenadorValidator

class EntrenadorService(private val entrenadorRepository: Repository<Entrenador, Long>) {

    fun crearEntrenador(nombre: String, email: String, especialidad: String): Entrenador {
        try {
            EntrenadorValidator.validarEntrenador(nombre, email, especialidad)
        } catch (e: ValidationException) {
            throw ValidationException("Error al crear entrenador: ${e.message}")
        }

        val entrenador = Entrenador(
            id = 0,
            nombre = nombre,
            email = email,
            especialidad = especialidad
        )

        return entrenadorRepository.create(entrenador)
    }

    fun obtenerEntrenador(id: Long): Entrenador {
        return entrenadorRepository.findById(id)
            ?: throw NotFoundException("Entrenador con ID $id no encontrado")
    }

    fun listarTodosLosEntrenadores(): List<Entrenador> {
        return entrenadorRepository.findAll()
    }

    fun listarEntrenadoresPorEspecialidad(especialidad: String): List<Entrenador> {
        return entrenadorRepository.findAll().filter {
            it.especialidad.equals(especialidad, ignoreCase = true)
        }
    }

    fun actualizarEntrenador(entrenador: Entrenador): Entrenador {
        obtenerEntrenador(entrenador.id)
        EntrenadorValidator.validarEntrenador(entrenador.nombre, entrenador.email, entrenador.especialidad)
        return entrenadorRepository.update(entrenador)
    }

    fun eliminarEntrenador(id: Long): Boolean {
        obtenerEntrenador(id)
        return entrenadorRepository.delete(id)
    }

    fun contarEntrenadores(): Int = entrenadorRepository.findAll().size
}