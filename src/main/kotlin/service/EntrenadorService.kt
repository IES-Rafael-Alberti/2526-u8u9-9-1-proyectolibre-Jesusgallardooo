package service

import model.Entrenador
import repository.Repository
import validator.EntrenadorValidator
import exception.NotFoundException

/**
 * Servicio que gestiona la lógica de negocio de los entrenadores.
 * Persiste simultáneamente en CSV y en H2.
 */
class EntrenadorService(
    private val csvRepo: Repository<Entrenador, Long>,
    private val sqlRepo: Repository<Entrenador, Long>
) {
    fun crearEntrenador(nombre: String, email: String, especialidad: String): Entrenador {
        EntrenadorValidator.validarEntrenador(nombre, email, especialidad)
        val entrenador = Entrenador(0, nombre, email, especialidad)
        csvRepo.create(entrenador)
        val sqlEntrenador = sqlRepo.create(entrenador)
        println("  Guardado en CSV y H2 (ID: ${sqlEntrenador.id})")
        return sqlEntrenador
    }

    fun obtenerEntrenador(id: Long): Entrenador =
        sqlRepo.findById(id) ?: csvRepo.findById(id) ?: throw NotFoundException("Entrenador con ID $id no encontrado")

    fun listarTodosLosEntrenadores(): List<Entrenador> = csvRepo.findAll()

    fun actualizarEntrenador(entrenador: Entrenador): Entrenador {
        EntrenadorValidator.validarEntrenador(entrenador.nombre, entrenador.email, entrenador.especialidad)
        csvRepo.update(entrenador)
        val updated = sqlRepo.update(entrenador)
        println("  Actualizado en CSV y H2")
        return updated
    }

    fun eliminarEntrenador(id: Long): Boolean {
        val csvOk = csvRepo.delete(id)
        val sqlOk = sqlRepo.delete(id)
        println("  Eliminado de CSV y H2")
        return csvOk && sqlOk
    }

    fun contarEntrenadores(): Int = csvRepo.findAll().size
}
