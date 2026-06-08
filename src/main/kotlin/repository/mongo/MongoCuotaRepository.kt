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

/**
 * Repositorio de cuotas con persistencia en MongoDB.
 * Almacena los documentos en la colección configurada en .env (por defecto "cuotas").
 */
class MongoCuotaRepository : Repository<Cuota, Long> {

    private val collection = MongodbManager.getCuotasCollection()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private fun cuotaToDocument(cuota: Cuota): Document = Document().apply {
        append("idCuota", cuota.id)
        append("socioId", cuota.socioId)
        append("importe", cuota.importe)
        append("fechaPago", cuota.fechaPago.format(dateFormatter))
    }

    private fun documentToCuota(doc: Document): Cuota = Cuota(
        id = doc.getLong("idCuota"),
        socioId = doc.getLong("socioId"),
        importe = doc.getDouble("importe"),
        fechaPago = LocalDate.parse(doc.getString("fechaPago"), dateFormatter)
    )

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
        collection.insertOne(cuotaToDocument(entity))
        return entity
    }

    override fun update(entity: Cuota): Cuota {
        val result = collection.updateOne(Filters.eq("idCuota", entity.id), Updates.combine(
            Updates.set("socioId", entity.socioId),
            Updates.set("importe", entity.importe),
            Updates.set("fechaPago", entity.fechaPago.format(dateFormatter))
        ))
        if (result.matchedCount == 0L) throw NotFoundException("Cuota con ID ${entity.id} no existe en MongoDB")
        return entity
    }

    override fun delete(id: Long): Boolean = collection.deleteOne(Filters.eq("idCuota", id)).deletedCount > 0

    fun findBySocioId(socioId: Long): List<Cuota> {
        val list = mutableListOf<Cuota>()
        collection.find(Filters.eq("socioId", socioId)).forEach { doc -> list.add(documentToCuota(doc)) }
        return list
    }
}
