package repository

import exception.NotFoundException
import model.Actividad
import model.Cuota
import model.Entrenador
import model.Inscripcion
import model.Socio

class InMemorySocioRepository : Repository<Socio, Long> {
    private val store = mutableMapOf<Long, Socio>()
    private var nextId = 1L
    override fun findAll() = store.values.toList()
    override fun findById(id: Long) = store[id]
    override fun create(entity: Socio): Socio {
        val id = nextId++; val copy = entity.copy(id = id); store[id] = copy; return copy
    }
    override fun update(entity: Socio): Socio {
        if (!store.containsKey(entity.id)) throw NotFoundException("Socio con ID ${entity.id} no encontrado")
        store[entity.id] = entity; return entity
    }
    override fun delete(id: Long) = store.remove(id) != null
    fun clear() { store.clear(); nextId = 1L }
}

class InMemoryActividadRepository : Repository<Actividad, Long> {
    private val store = mutableMapOf<Long, Actividad>()
    private var nextId = 1L
    override fun findAll() = store.values.toList()
    override fun findById(id: Long) = store[id]
    override fun create(entity: Actividad): Actividad {
        val id = nextId++; val copy = entity.copy(id = id); store[id] = copy; return copy
    }
    override fun update(entity: Actividad): Actividad {
        if (!store.containsKey(entity.id)) throw NotFoundException("Actividad con ID ${entity.id} no encontrada")
        store[entity.id] = entity; return entity
    }
    override fun delete(id: Long) = store.remove(id) != null
    fun clear() { store.clear(); nextId = 1L }
}

class InMemoryEntrenadorRepository : Repository<Entrenador, Long> {
    private val store = mutableMapOf<Long, Entrenador>()
    private var nextId = 1L
    override fun findAll() = store.values.toList()
    override fun findById(id: Long) = store[id]
    override fun create(entity: Entrenador): Entrenador {
        val id = nextId++; val copy = entity.copy(id = id); store[id] = copy; return copy
    }
    override fun update(entity: Entrenador): Entrenador {
        if (!store.containsKey(entity.id)) throw NotFoundException("Entrenador con ID ${entity.id} no encontrado")
        store[entity.id] = entity; return entity
    }
    override fun delete(id: Long) = store.remove(id) != null
    fun clear() { store.clear(); nextId = 1L }
}

class InMemoryCuotaRepository : Repository<Cuota, Long> {
    private val store = mutableMapOf<Long, Cuota>()
    private var nextId = 1L
    override fun findAll() = store.values.toList()
    override fun findById(id: Long) = store[id]
    override fun create(entity: Cuota): Cuota {
        val id = nextId++; val copy = entity.copy(id = id); store[id] = copy; return copy
    }
    override fun update(entity: Cuota): Cuota {
        if (!store.containsKey(entity.id)) throw NotFoundException("Cuota con ID ${entity.id} no encontrada")
        store[entity.id] = entity; return entity
    }
    override fun delete(id: Long) = store.remove(id) != null
    fun clear() { store.clear(); nextId = 1L }
}

class InMemoryInscripcionRepository : Repository<Inscripcion, Long> {
    private val store = mutableMapOf<Long, Inscripcion>()
    private var nextId = 1L
    override fun findAll() = store.values.toList()
    override fun findById(id: Long) = store[id]
    override fun create(entity: Inscripcion): Inscripcion {
        val id = nextId++; val copy = entity.copy(id = id); store[id] = copy; return copy
    }
    override fun update(entity: Inscripcion): Inscripcion {
        if (!store.containsKey(entity.id)) throw NotFoundException("Inscripcion con ID ${entity.id} no encontrada")
        store[entity.id] = entity; return entity
    }
    override fun delete(id: Long) = store.remove(id) != null
    fun clear() { store.clear(); nextId = 1L }
}
