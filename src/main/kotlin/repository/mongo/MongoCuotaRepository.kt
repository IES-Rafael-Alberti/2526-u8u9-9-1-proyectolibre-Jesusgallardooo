// repository/mongo/MongoCuotaRepository.kt
package repository.mongo

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import model.Cuota
import repository.Repository
import exception.NotFoundException
import org.bson.Document
import util.MongodbManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MongoCuotaRepository : Repository<Cuota, Long> {

    private val collection = MongodbManager.getCuotasCollection()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    // Conversiones
    private fun cuotaToDocument(cuota: Cuota): Document {
        return Document().apply {
            append("idCuota", cuota.id)          // campo numérico para buscar por ID
            append("socioId", cuota.socioId)
            append("importe", cuota.importe)
            append("fechaPago", cuota.fechaPago.format(dateFormatter))
        }
    }

    private fun documentToCuota(doc: Document): Cuota {
        return Cuota(
            id = doc.getLong("idCuota"),
            socioId = doc.getLong("socioId"),
            importe = doc.getDouble("importe"),
            fechaPago = LocalDate.parse(doc.getString("fechaPago"), dateFormatter)
        )
    }

    override fun findAll(): List<Cuota> {
        val list = mutableListOf<Cuota>()
        collection.find().forEach { doc -> list.add(documentToCuota(doc)) }
        return list
    }

    override fun findById(id: Long): Cuota? {
        val doc = collection.find(Filters.eq("idCuota", id)).first()
        return doc?.let { documentToCuota(it) }
    }

    override fun create(entity: Cuota): Cuota {
        val doc = cuotaToDocument(entity)
        collection.insertOne(doc)
        return entity  // el ID ya viene asignado desde CSV
    }

    override fun update(entity: Cuota): Cuota {
        val filter = Filters.eq("idCuota", entity.id)
        val update = Updates.combine(
            Updates.set("socioId", entity.socioId),
            Updates.set("importe", entity.importe),
            Updates.set("fechaPago", entity.fechaPago.format(dateFormatter))
        )
        val result = collection.updateOne(filter, update)
        if (result.matchedCount == 0L) {
            throw NotFoundException("Cuota con ID ${entity.id} no existe en MongoDB")
        }
        return entity
    }

    override fun delete(id: Long): Boolean {
        val result = collection.deleteOne(Filters.eq("idCuota", id))
        return result.deletedCount > 0
    }

    // Método extra para facilitar búsquedas por socio
    fun findBySocioId(socioId: Long): List<Cuota> {
        val list = mutableListOf<Cuota>()
        collection.find(Filters.eq("socioId", socioId)).forEach { doc ->
            list.add(documentToCuota(doc))
        }
        return list
    }
}