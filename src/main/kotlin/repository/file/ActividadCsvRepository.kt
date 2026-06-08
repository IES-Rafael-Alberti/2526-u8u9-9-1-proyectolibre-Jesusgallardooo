package repository.file

import model.Actividad
import repository.Repository
import java.io.File

class ActividadCsvRepository : Repository<Actividad, Long> {

    private val file = File("data/actividades.csv")
    private val actividades = mutableMapOf<Long, Actividad>()
    private var nextId = 1L

    init { cargar() }

    private fun cargar() {
        if (!file.exists()) return
        try {
            val lines = file.readLines()
            if (lines.size <= 1) return
            for (line in lines.drop(1)) {
                val parts = line.split(",")
                if (parts.size >= 3) {
                    val id = parts[0].toLong()
                    actividades[id] = Actividad(id, parts[1], parts[2].toInt())
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) { println("Error al cargar CSV de actividades: ${e.message}") }
    }

    private fun guardar() {
        try {
            val lines = mutableListOf("id,nombre,plazasMaximas")
            actividades.values.sortedBy { it.id }.forEach { a ->
                lines.add("${a.id},${a.nombre},${a.plazasMaximas}")
            }
            file.writeText(lines.joinToString("\n"))
        } catch (e: Exception) { println("Error al guardar CSV: ${e.message}") }
    }

    override fun findAll(): List<Actividad> = actividades.values.toList().sortedBy { it.id }
    override fun findById(id: Long): Actividad? = actividades[id]

    override fun create(entity: Actividad): Actividad {
        val id = nextId++; val copy = entity.copy(id = id); actividades[id] = copy; guardar(); return copy
    }

    override fun update(entity: Actividad): Actividad { actividades[entity.id] = entity; guardar(); return entity }

    override fun delete(id: Long): Boolean { val r = actividades.remove(id); if (r != null) guardar(); return r != null }
}
