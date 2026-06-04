package repository.memoryTests

import exception.NotFoundException
import model.Socio
import repository.Repository

class InMemorySocioRepository : Repository<Socio, Long> {

    // Almacenamiento en memoria
    private val socios = mutableMapOf<Long, Socio>()
    private var currentId = 1L

    override fun findAll(): List<Socio> {
        return socios.values.toList()
    }

    override fun findById(id: Long): Socio? {
        return socios[id]
    }

    override fun create(entity: Socio): Socio {
        val newSocio = entity.copy(id = currentId)
        socios[currentId] = newSocio
        currentId++
        return newSocio
    }

    override fun update(entity: Socio): Socio {
        if (!socios.containsKey(entity.id)) {
            throw NotFoundException("No se puede actualizar: Socio con ID ${entity.id} no existe")
        }
        socios[entity.id] = entity
        return entity
    }

    override fun delete(id: Long): Boolean {
        return socios.remove(id) != null
    }
}