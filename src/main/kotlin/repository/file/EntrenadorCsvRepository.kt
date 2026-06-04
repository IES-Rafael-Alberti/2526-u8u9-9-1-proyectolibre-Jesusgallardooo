package repository.file

// repository/file/CsvEntrenadorRepository.kt

import model.Entrenador
import repository.Repository
import exception.NotFoundException
import java.io.File

class EntrenadorCsvRepository(
    private val filePath: String = "data/entrenadores.csv"
) : Repository<Entrenador, Long> {

    private val entrenadores: MutableMap<Long, Entrenador> = mutableMapOf()
    private var nextId: Long = 1L

    init {
        cargarDeCsv()
    }

    private fun cargarDeCsv() {
        val file = File(filePath)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
            return
        }

        try {
            file.forEachLine { line ->
                if (line.isNotBlank() && !line.startsWith("id,")) {
                    val partes = line.split(",")
                    if (partes.size >= 4) {
                        val id = partes[0].toLong()
                        val nombre = partes[1]
                        val email = partes[2]
                        val especialidad = partes[3]

                        entrenadores[id] = Entrenador(id, nombre, email, especialidad)
                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al cargar CSV de entrenadores: ${e.message}")
        }
    }

    private fun guardarEnCsv() {
        val file = File(filePath)
        file.parentFile?.mkdirs()

        file.printWriter().use { out ->
            out.println("id,nombre,email,especialidad")
            entrenadores.values.sortedBy { it.id }.forEach { entrenador ->
                out.println("${entrenador.id},${entrenador.nombre},${entrenador.email},${entrenador.especialidad}")
            }
        }
    }

    override fun findAll(): List<Entrenador> = entrenadores.values.toList()
    override fun findById(id: Long): Entrenador? = entrenadores[id]

    override fun create(entity: Entrenador): Entrenador {
        val newEntity = entity.copy(id = nextId)
        entrenadores[nextId] = newEntity
        nextId++
        guardarEnCsv()
        return newEntity
    }

    override fun update(entity: Entrenador): Entrenador {
        if (!entrenadores.containsKey(entity.id)) {
            throw NotFoundException("Entrenador con ID ${entity.id} no existe")
        }
        entrenadores[entity.id] = entity
        guardarEnCsv()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = entrenadores.remove(id) != null
        if (result) guardarEnCsv()
        return result
    }
}