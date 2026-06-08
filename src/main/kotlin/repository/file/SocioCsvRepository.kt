package repository.file

import model.Socio
import repository.Repository
import java.io.File

/**
 * Repositorio de socios con persistencia en fichero CSV.
 * El fichero se encuentra en data/socios.csv y se reescribe completo en cada operación de escritura.
 */
class SocioCsvRepository : Repository<Socio, Long> {

    private val file = File("data/socios.csv")
    private val socios = mutableMapOf<Long, Socio>()
    private var nextId = 1L

    init {
        cargar()
    }

    private fun cargar() {
        if (!file.exists()) return
        try {
            val lines = file.readLines()
            if (lines.size <= 1) return
            for (line in lines.drop(1)) {
                val parts = line.split(",")
                if (parts.size >= 6) {
                    val id = parts[0].toLong()
                    val socio = Socio(id, parts[1], parts[2], parts[3], parts[4], parts[5].toBoolean())
                    socios[id] = socio
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) {
            println("Error al cargar CSV de socios: ${e.message}")
        }
    }

    private fun guardar() {
        try {
            val lines = mutableListOf("id,nombre,apellido,email,telefono,activo")
            socios.values.sortedBy { it.id }.forEach { s ->
                lines.add("${s.id},${s.nombre},${s.apellido},${s.email},${s.telefono},${s.activo}")
            }
            file.writeText(lines.joinToString("\n"))
        } catch (e: Exception) {
            println("Error al guardar CSV de socios: ${e.message}")
        }
    }

    override fun findAll(): List<Socio> = socios.values.toList().sortedBy { it.id }

    override fun findById(id: Long): Socio? = socios[id]

    override fun create(entity: Socio): Socio {
        val id = nextId++
        val copy = entity.copy(id = id)
        socios[id] = copy
        guardar()
        return copy
    }

    override fun update(entity: Socio): Socio {
        socios[entity.id] = entity
        guardar()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val removed = socios.remove(id)
        if (removed != null) guardar()
        return removed != null
    }
}
