package util

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document

object MongodbManager {
    private val dotenv = dotenv()
    private val uri = dotenv["MONGODB_URI"]           // mongodb+srv://...
    private val databaseName = dotenv["MONGODB_DATABASE"]  // gymManager
    private val collectionName = dotenv["MONGODB_COLLECTION"] // cuotas

    private var mongoClient: MongoClient? = null

    fun getDatabase(): MongoDatabase {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(uri)
        }
        return mongoClient!!.getDatabase(databaseName)
    }

    fun getCuotasCollection(): MongoCollection<Document> {
        return getDatabase().getCollection(collectionName)
    }

    fun closeConnection() {
        mongoClient?.close()
        mongoClient = null
    }

    fun testConnection(): Boolean {
        return try {
            getDatabase().listCollectionNames().first()
            println("Conectado a MongoDB (${databaseName}.${collectionName})")
            true
        } catch (e: Exception) {
            println("Error MongoDB: ${e.message}")
            false
        }
    }
}