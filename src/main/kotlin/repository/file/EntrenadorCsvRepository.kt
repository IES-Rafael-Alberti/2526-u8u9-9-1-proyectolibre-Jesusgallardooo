package repository.file

import model.Entrenador
import repository.Repository
import java.io.File

class EntrenadorCsvRepository : Repository<Entrenador, Long> {

    private val file = File("data/entrenadores.csv")
    private val entrenadores = mutableMapOf<Long, Entrenador>()
    private var nextId = 1L

    init { cargar() }

    private fun cargar() {
        if (!file.exists()) return
        try {
            val lines = file.readLines()
            if (lines.size <= 1) return
            for (line in lines.drop(1)) {
                val parts = line.split(",")
                if (parts.size >= 4) {
                    val id = parts[0].toLong()
                    entrenadores[id] = Entrenador(id, parts[1], parts[2], parts[3])
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) { println("Error al cargar CSV: ${e.message}") }
    }

    private fun guardar() {
        try {
            val lines = mutableListOf("id,nombre,email,especialidad")
            entrenadores.values.sortedBy { it.id }.forEach { e ->
                lines.add("${e.id},${e.nombre},${e.email},${e.especialidad}")
            }
            file.writeText(lines.joinToString("\n"))
        } catch (e: Exception) { println("Error al guardar CSV: ${e.message}") }
    }

    override fun findAll(): List<Entrenador> = entrenadores.values.toList().sortedBy { it.id }
    override fun findById(id: Long): Entrenador? = entrenadores[id]

    override fun create(entity: Entrenador): Entrenador {
        val id = nextId++; val copy = entity.copy(id = id); entrenadores[id] = copy; guardar(); return copy
    }

    override fun update(entity: Entrenador): Entrenador { entrenadores[entity.id] = entity; guardar(); return entity }

    override fun delete(id: Long): Boolean { val r = entrenadores.remove(id); if (r != null) guardar(); return r != null }
}
