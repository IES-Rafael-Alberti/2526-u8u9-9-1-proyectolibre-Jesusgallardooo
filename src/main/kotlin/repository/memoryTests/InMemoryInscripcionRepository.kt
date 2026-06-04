package repository.memoryTests
// repository/memory/InMemoryInscripcionRepository.kt


import exception.NotFoundException
import model.Inscripcion
import repository.Repository

class InMemoryInscripcionRepository : Repository<Inscripcion, Long> {

    private val inscripciones = mutableMapOf<Long, Inscripcion>()
    private var currentId = 1L

    override fun findAll(): List<Inscripcion> = inscripciones.values.toList()

    override fun findById(id: Long): Inscripcion? = inscripciones[id]

    override fun create(entity: Inscripcion): Inscripcion {
        val newEntity = entity.copy(id = currentId)
        inscripciones[currentId] = newEntity
        currentId++
        return newEntity
    }

    override fun update(entity: Inscripcion): Inscripcion {
        if (!inscripciones.containsKey(entity.id)) {
            throw NotFoundException("Inscripción con ID ${entity.id} no existe")
        }
        inscripciones[entity.id] = entity
        return entity
    }

    override fun delete(id: Long): Boolean = inscripciones.remove(id) != null

    fun clear() = inscripciones.clear()
    fun count(): Int = inscripciones.size
}