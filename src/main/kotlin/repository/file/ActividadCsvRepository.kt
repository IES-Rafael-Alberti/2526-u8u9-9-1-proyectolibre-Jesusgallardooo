package repository.file

// repository/file/CsvActividadRepository.kt
import model.Actividad
import repository.Repository
import exception.NotFoundException
import java.io.File

class ActividadCsvRepository(
    private val filePath: String = "data/actividades.csv"
) : Repository<Actividad, Long> {

    private val actividades: MutableMap<Long, Actividad> = mutableMapOf()
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
                    if (partes.size >= 3) {
                        val id = partes[0].toLong()
                        val nombre = partes[1]
                        val plazasMaximas = partes[2].toInt()

                        actividades[id] = Actividad(id, nombre, plazasMaximas)
                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al cargar CSV de actividades: ${e.message}")
        }
    }

    private fun guardarEnCsv() {
        val file = File(filePath)
        file.parentFile?.mkdirs()

        file.printWriter().use { out ->
            out.println("id,nombre,plazas_maximas")
            actividades.values.sortedBy { it.id }.forEach { actividad ->
                out.println("${actividad.id},${actividad.nombre},${actividad.plazasMaximas}")
            }
        }
    }

    override fun findAll(): List<Actividad> = actividades.values.toList()
    override fun findById(id: Long): Actividad? = actividades[id]

    override fun create(entity: Actividad): Actividad {
        val newEntity = entity.copy(id = nextId)
        actividades[nextId] = newEntity
        nextId++
        guardarEnCsv()
        return newEntity
    }

    override fun update(entity: Actividad): Actividad {
        if (!actividades.containsKey(entity.id)) {
            throw NotFoundException("Actividad con ID ${entity.id} no existe")
        }
        actividades[entity.id] = entity
        guardarEnCsv()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = actividades.remove(id) != null
        if (result) guardarEnCsv()
        return result
    }
}