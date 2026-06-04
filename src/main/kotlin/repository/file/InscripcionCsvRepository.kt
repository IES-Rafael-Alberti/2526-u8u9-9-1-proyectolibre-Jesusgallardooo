package repository.file

// repository/file/CsvInscripcionRepository.kt

import model.Inscripcion
import repository.Repository
import exception.NotFoundException
import java.io.File
import java.time.LocalDate

class InscripcionCsvRepository(
    private val filePath: String = "data/inscripciones.csv"
) : Repository<Inscripcion, Long> {

    private val inscripciones: MutableMap<Long, Inscripcion> = mutableMapOf()
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
                        val socioId = partes[1].toLong()
                        val actividadId = partes[2].toLong()
                        val fechaInscricao = LocalDate.parse(partes[3])

                        inscripciones[id] = Inscripcion(id, socioId, actividadId, fechaInscricao)
                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al cargar CSV de inscripciones: ${e.message}")
        }
    }

    private fun guardarEnCsv() {
        val file = File(filePath)
        file.parentFile?.mkdirs()

        file.printWriter().use { out ->
            out.println("id,socio_id,actividad_id,fecha_inscripcion")
            inscripciones.values.sortedBy { it.id }.forEach { inscripcion ->
                out.println("${inscripcion.id},${inscripcion.socioId},${inscripcion.actividadId},${inscripcion.fechaInscricao}")
            }
        }
    }

    override fun findAll(): List<Inscripcion> = inscripciones.values.toList()
    override fun findById(id: Long): Inscripcion? = inscripciones[id]

    override fun create(entity: Inscripcion): Inscripcion {
        val newEntity = entity.copy(id = nextId)
        inscripciones[nextId] = newEntity
        nextId++
        guardarEnCsv()
        return newEntity
    }

    override fun update(entity: Inscripcion): Inscripcion {
        if (!inscripciones.containsKey(entity.id)) {
            throw NotFoundException("Inscripción con ID ${entity.id} no existe")
        }
        inscripciones[entity.id] = entity
        guardarEnCsv()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = inscripciones.remove(id) != null
        if (result) guardarEnCsv()
        return result
    }
}