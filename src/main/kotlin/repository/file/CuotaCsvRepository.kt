package repository.file

import model.Cuota
import repository.Repository
import java.io.File
import java.time.LocalDate

class CuotaCsvRepository : Repository<Cuota, Long> {

    private val file = File("data/cuotas.csv")
    private val cuotas = mutableMapOf<Long, Cuota>()
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
                    cuotas[id] = Cuota(id, parts[1].toLong(), parts[2].toDouble(), LocalDate.parse(parts[3]))
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) { println("Error al cargar CSV: ${e.message}") }
    }

    private fun guardar() {
        try {
            val lines = mutableListOf("id,socioId,importe,fechaPago")
            cuotas.values.sortedBy { it.id }.forEach { c ->
                lines.add("${c.id},${c.socioId},${c.importe},${c.fechaPago}")
            }
            file.writeText(lines.joinToString("\n"))
        } catch (e: Exception) { println("Error al guardar CSV: ${e.message}") }
    }

    override fun findAll(): List<Cuota> = cuotas.values.toList().sortedBy { it.id }
    override fun findById(id: Long): Cuota? = cuotas[id]

    override fun create(entity: Cuota): Cuota {
        val id = nextId++; val copy = entity.copy(id = id); cuotas[id] = copy; guardar(); return copy
    }

    override fun update(entity: Cuota): Cuota { cuotas[entity.id] = entity; guardar(); return entity }

    override fun delete(id: Long): Boolean { val r = cuotas.remove(id); if (r != null) guardar(); return r != null }
}
