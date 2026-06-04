package repository.memoryTests

// repository/memory/InMemorySocioRepository.kt

// repository/memory/InMemoryCuotaRepository.kt
import exception.NotFoundException
import model.Cuota
import repository.Repository

class InMemoryCuotaRepository : Repository<Cuota, Long> {

    private val cuotas = mutableMapOf<Long, Cuota>()
    private var currentId = 1L

    override fun findAll(): List<Cuota> = cuotas.values.toList()

    override fun findById(id: Long): Cuota? = cuotas[id]

    override fun create(entity: Cuota): Cuota {
        val newEntity = entity.copy(id = currentId)
        cuotas[currentId] = newEntity
        currentId++
        return newEntity
    }

    override fun update(entity: Cuota): Cuota {
        if (!cuotas.containsKey(entity.id)) {
            throw NotFoundException("Cuota con ID ${entity.id} no existe")
        }
        cuotas[entity.id] = entity
        return entity
    }

    override fun delete(id: Long): Boolean = cuotas.remove(id) != null

    fun clear() = cuotas.clear()
    fun count(): Int = cuotas.size
}