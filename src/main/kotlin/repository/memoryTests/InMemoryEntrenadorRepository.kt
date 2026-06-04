package repository.memoryTests
// repository/memory/InMemoryEntrenadorRepository.kt

import model.Entrenador
import repository.Repository
import exception.NotFoundException

class InMemoryEntrenadorRepository : Repository<Entrenador, Long> {

    private val entrenadores = mutableMapOf<Long, Entrenador>()
    private var currentId = 1L

    override fun findAll(): List<Entrenador> = entrenadores.values.toList()

    override fun findById(id: Long): Entrenador? = entrenadores[id]

    override fun create(entity: Entrenador): Entrenador {
        val newEntity = entity.copy(id = currentId)
        entrenadores[currentId] = newEntity
        currentId++
        return newEntity
    }

    override fun update(entity: Entrenador): Entrenador {
        if (!entrenadores.containsKey(entity.id)) {
            throw NotFoundException("Entrenador con ID ${entity.id} no existe")
        }
        entrenadores[entity.id] = entity
        return entity
    }

    override fun delete(id: Long): Boolean = entrenadores.remove(id) != null
}