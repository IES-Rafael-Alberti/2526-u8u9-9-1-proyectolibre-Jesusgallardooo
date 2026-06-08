package repository.file

import model.Inscripcion
import repository.Repository
import java.io.File
import java.time.LocalDate

class InscripcionCsvRepository : Repository<Inscripcion, Long> {

    private val file = File("data/inscripciones.csv")
    private val inscripciones = mutableMapOf<Long, Inscripcion>()
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
                    inscripciones[id] = Inscripcion(id, parts[1].toLong(), parts[2].toLong(), LocalDate.parse(parts[3]))
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) { println("Error al cargar CSV: ${e.message}") }
    }

    private fun guardar() {
        try {
            val lines = mutableListOf("id,socioId,actividadId,fechaInscripcion")
            inscripciones.values.sortedBy { it.id }.forEach { i ->
                lines.add("${i.id},${i.socioId},${i.actividadId},${i.fechaInscripcion}")
            }
            file.writeText(lines.joinToString("\n"))
        } catch (e: Exception) { println("Error al guardar CSV: ${e.message}") }
    }

    override fun findAll(): List<Inscripcion> = inscripciones.values.toList().sortedBy { it.id }
    override fun findById(id: Long): Inscripcion? = inscripciones[id]

    override fun create(entity: Inscripcion): Inscripcion {
        val id = nextId++; val copy = entity.copy(id = id); inscripciones[id] = copy; guardar(); return copy
    }

    override fun update(entity: Inscripcion): Inscripcion { inscripciones[entity.id] = entity; guardar(); return entity }

    override fun delete(id: Long): Boolean { val r = inscripciones.remove(id); if (r != null) guardar(); return r != null }
}
