package org.iesra.repository.mongo

import com.mongodb.MongoException
import com.mongodb.MongoSocketException
import com.mongodb.MongoTimeoutException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.types.ObjectId
import org.iesra.model.HistorialMedico
import org.slf4j.LoggerFactory

class HistorialRepositoryMongo(
    private val collection: MongoCollection<Document> = MongoClientProvider.getCollection("historial_mascota")
) {

    private val log = LoggerFactory.getLogger(HistorialRepositoryMongo::class.java)

    fun save(historial: HistorialMedico): HistorialMedico? {
        if (historial.id != null) return null
        return try {
            withRetry {
                val objectId = ObjectId()
                val doc = Document("_id", objectId)
                    .append("idMascota", historial.idMascota)
                    .append("descripcion", historial.descripcion)
                    .append("diagnostico", historial.diagnostico)
                    .append("tratamiento", historial.tratamiento)
                    .append("fecha", historial.fecha)
                collection.insertOne(doc)
                historial.copy(id = objectId.toHexString())
            }
        } catch (e: MongoRepositoryException) {
            log.error("save(): No se pudo insertar el historial: ${e.message}")
            null
        }
    }

    fun findAll(): List<HistorialMedico> {
        return try {
            withRetry {
                collection.find()
                    .map { doc -> toHistorialMedico(doc) }
                    .toList()
            }
        } catch (e: MongoRepositoryException) {
            log.error("findAll(): No se pudieron recuperar los historiales: ${e.message}")
            emptyList()
        }
    }

    fun findByMascotaId(mascotaId: Int): List<HistorialMedico> {
        return try {
            withRetry {
                collection.find(Filters.eq("idMascota", mascotaId))
                    .map { doc -> toHistorialMedico(doc) }
                    .toList()
            }
        } catch (e: MongoRepositoryException) {
            log.error("findByMascotaId($mascotaId): No se pudieron recuperar los historiales: ${e.message}")
            emptyList()
        }
    }

    fun update(historial: HistorialMedico): Boolean {
        val id = historial.id ?: return false
        return try {
            withRetry {
                val objectId = ObjectId(id)
                val doc = Document("_id", objectId)
                    .append("idMascota", historial.idMascota)
                    .append("descripcion", historial.descripcion)
                    .append("diagnostico", historial.diagnostico)
                    .append("tratamiento", historial.tratamiento)
                    .append("fecha", historial.fecha)
                collection.replaceOne(Filters.eq("_id", objectId), doc).modifiedCount > 0
            }
        } catch (e: MongoRepositoryException) {
            log.error("update(): No se pudo actualizar el historial $id: ${e.message}")
            false
        }
    }

    fun delete(id: String): Boolean {
        return try {
            withRetry {
                collection.deleteOne(Filters.eq("_id", ObjectId(id))).deletedCount > 0
            }
        } catch (e: MongoRepositoryException) {
            log.error("delete(): No se pudo eliminar el historial $id: ${e.message}")
            false
        }
    }

    private fun <T> withRetry(operation: () -> T): T {
        val maxRetries = 3
        var lastException: MongoException? = null

        for (attempt in 1..maxRetries) {
            try {
                return operation()
            } catch (e: MongoTimeoutException) {
                log.warn("Timeout en intento $attempt/$maxRetries: ${e.message}")
                lastException = e
                if (attempt < maxRetries) sleepBeforeRetry(attempt)
            } catch (e: MongoSocketException) {
                log.warn("Error SSL/socket en intento $attempt/$maxRetries: ${e.message}")
                lastException = e
                if (attempt < maxRetries) sleepBeforeRetry(attempt)
            } catch (e: MongoException) {
                log.error("Error Mongo en intento $attempt/$maxRetries: ${e.message}")
                lastException = e
                if (attempt < maxRetries) sleepBeforeRetry(attempt)
            }
        }

        throw MongoRepositoryException(
            "Operación falló tras $maxRetries intentos. Último error: ${lastException?.message}",
            lastException
        )
    }

    private fun sleepBeforeRetry(attempt: Int) {
        try {
            Thread.sleep(1000L * attempt)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private fun toHistorialMedico(doc: Document): HistorialMedico {
        return HistorialMedico(
            id = doc.getObjectId("_id").toHexString(),
            idMascota = doc.getInteger("idMascota"),
            descripcion = doc.getString("descripcion"),
            diagnostico = doc.getString("diagnostico"),
            tratamiento = doc.getString("tratamiento"),
            fecha = doc.getDate("fecha")
        )
    }
}
