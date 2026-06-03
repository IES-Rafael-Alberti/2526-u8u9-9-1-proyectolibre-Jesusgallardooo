package org.iesra.repository.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.iesra.util.MongoConfig
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object MongoClientProvider {

    private val log = LoggerFactory.getLogger(MongoClientProvider::class.java)
    private val config: MongoConfig by lazy { MongoConfig.fromEnv() }

    val client: MongoClient by lazy {
        log.info("Iniciando conexión a MongoDB Atlas...")

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(config.connectionString))
            .applyToSslSettings { ssl ->
                ssl.enabled(true)
            }
            .applyToConnectionPoolSettings { pool ->
                pool.minSize(0)
                    .maxSize(5)
                    .maxConnectionIdleTime(30, TimeUnit.SECONDS)
                    .maxConnectionLifeTime(5, TimeUnit.MINUTES)
                    .maintenanceInitialDelay(5, TimeUnit.SECONDS)
                    .maintenanceFrequency(30, TimeUnit.SECONDS)
            }
            .applyToSocketSettings { socket ->
                socket.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
            }
            .applyToClusterSettings { cluster ->
                cluster.serverSelectionTimeout(10, TimeUnit.SECONDS)
            }
            .retryWrites(true)
            .retryReads(true)
            .build()

        val c = MongoClients.create(settings)

        log.info("Conexión a MongoDB Atlas establecida correctamente.")

        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Cerrando conexión MongoDB Atlas...")
            c.close()
            log.info("Conexión MongoDB Atlas cerrada.")
        })

        c
    }

    fun getCollection(name: String): MongoCollection<Document> {
        log.debug("Obteniendo colección: $name")
        return client.getDatabase(config.database).getCollection(name)
    }
}
