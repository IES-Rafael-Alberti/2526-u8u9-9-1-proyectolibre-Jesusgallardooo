package org.iesra.util

import java.io.File

data class MongoConfig(
    val connectionString: String,
    val database: String
) {
    companion object {
        fun fromEnv(): MongoConfig {
            val env = loadEnv()
            return MongoConfig(
                connectionString = env["MONGO_URI"]
                    ?: throw IllegalStateException("MONGO_URI no definida en .env"),
                database = env["MONGO_DB"]
                    ?: throw IllegalStateException("MONGO_DB no definida en .env")
            )
        }

        private fun loadEnv(): Map<String, String> {
            val env = mutableMapOf<String, String>()
            val envFile = File(".env")
            if (envFile.exists()) {
                envFile.readLines().forEach { line ->
                    val trimmed = line.trim()
                    if (trimmed.isNotBlank() && !trimmed.startsWith("#")) {
                        val parts = trimmed.split("=", limit = 2)
                        if (parts.size == 2) {
                            env[parts[0].trim()] = parts[1].trim()
                        }
                    }
                }
            }
            System.getenv().forEach { (key, value) -> env[key] = value }
            return env
        }
    }
}
