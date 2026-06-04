package repository.file

// repository/file/CsvCuotaRepository.kt

import model.Cuota
import repository.Repository
import exception.NotFoundException
import java.io.File
import java.time.LocalDate

class CuotaCsvRepository(
    private val filePath: String = "data/cuotas.csv"
) : Repository<Cuota, Long> {

    private val cuotas: MutableMap<Long, Cuota> = mutableMapOf()
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
                        val importe = partes[2].toDouble()
                        val fechaPago = LocalDate.parse(partes[3])

                        cuotas[id] = Cuota(id, socioId, importe, fechaPago)
                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al cargar CSV de cuotas: ${e.message}")
        }
    }

    private fun guardarEnCsv() {
        val file = File(filePath)
        file.parentFile?.mkdirs()

        file.printWriter().use { out ->
            out.println("id,socio_id,importe,fecha_pago")
            cuotas.values.sortedBy { it.id }.forEach { cuota ->
                out.println("${cuota.id},${cuota.socioId},${cuota.importe},${cuota.fechaPago}")
            }
        }
    }

    override fun findAll(): List<Cuota> = cuotas.values.toList()
    override fun findById(id: Long): Cuota? = cuotas[id]

    override fun create(entity: Cuota): Cuota {
        val newEntity = entity.copy(id = nextId)
        cuotas[nextId] = newEntity
        nextId++
        guardarEnCsv()
        return newEntity
    }

    override fun update(entity: Cuota): Cuota {
        if (!cuotas.containsKey(entity.id)) {
            throw NotFoundException("Cuota con ID ${entity.id} no existe")
        }
        cuotas[entity.id] = entity
        guardarEnCsv()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = cuotas.remove(id) != null
        if (result) guardarEnCsv()
        return result
    }
}