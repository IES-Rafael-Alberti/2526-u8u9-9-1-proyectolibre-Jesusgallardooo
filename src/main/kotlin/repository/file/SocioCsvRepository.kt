package repository.file

// repository/file/CsvSocioRepository.kt
import model.Socio
import repository.Repository
import exception.NotFoundException
import java.io.File

class SocioCsvRepository(
    private val filePath: String = "data/socios.csv"
) : Repository<Socio, Long> {

    private val socios: MutableMap<Long, Socio> = mutableMapOf()
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
                if (line.isNotBlank() && !line.startsWith("id,")) { // Saltar cabecera
                    val partes = line.split(",")
                    if (partes.size >= 6) {
                        val id = partes[0].toLong()
                        val nombre = partes[1]
                        val apellido = partes[2]
                        val email = partes[3]
                        val telefono = partes[4]
                        val activo = partes[5].toBoolean()

                        val socio = Socio(id, nombre, apellido, email, telefono, activo)
                        socios[id] = socio

                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al cargar CSV: ${e.message}")
        }
    }

    private fun guardarEnCsv() {
        val file = File(filePath)
        file.parentFile?.mkdirs()

        file.printWriter().use { out ->
            // Escribir cabecera
            out.println("id,nombre,apellido,email,telefono,activo")

            // Escribir datos
            socios.values.sortedBy { it.id }.forEach { socio ->
                out.println("${socio.id},${socio.nombre},${socio.apellido},${socio.email},${socio.telefono},${socio.activo}")
            }
        }
    }

    override fun findAll(): List<Socio> = socios.values.toList()

    override fun findById(id: Long): Socio? = socios[id]

    override fun create(entity: Socio): Socio {
        val newSocio = entity.copy(id = nextId)
        socios[nextId] = newSocio
        nextId++
        guardarEnCsv()
        return newSocio
    }

    override fun update(entity: Socio): Socio {
        if (!socios.containsKey(entity.id)) {
            throw NotFoundException("Socio con ID ${entity.id} no existe")
        }
        socios[entity.id] = entity
        guardarEnCsv()
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = socios.remove(id) != null
        if (result) guardarEnCsv()
        return result
    }
}