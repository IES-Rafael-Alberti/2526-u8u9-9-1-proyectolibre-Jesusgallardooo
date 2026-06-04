package repository.memoryTests

// repository/memory/InMemoryActividadRepository.kt
import exception.NotFoundException
import model.Actividad
import repository.Repository

class InMemoryActividadRepository : Repository<Actividad, Long> {

    private val actividades = mutableMapOf<Long, Actividad>()
    private var currentId = 1L

    override fun findAll(): List<Actividad> = actividades.values.toList()

    override fun findById(id: Long): Actividad? = actividades[id]

    override fun create(entity: Actividad): Actividad {
        val newEntity = entity.copy(id = currentId)
        actividades[currentId] = newEntity
        currentId++
        return newEntity
    }

    override fun update(entity: Actividad): Actividad {
        if (!actividades.containsKey(entity.id)) {
            throw NotFoundException("Actividad con ID ${entity.id} no existe")
        }
        actividades[entity.id] = entity
        return entity
    }

    override fun delete(id: Long): Boolean = actividades.remove(id) != null
}