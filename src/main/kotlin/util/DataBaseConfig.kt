package org.iesra.util

import java.io.File

data class DataBaseConfig(
    val url: String,
    val user: String,
    val password: String
) {
    companion object {
        fun fromEnv(): DataBaseConfig {
            val env = loadEnv()
            return DataBaseConfig(
                url = env["H2_URL"] ?: throw IllegalStateException("H2_URL no definida en .env"),
                user = env["H2_USER"] ?: throw IllegalStateException("H2_USER no definida en .env"),
                password = env["H2_PASSWORD"] ?: throw IllegalStateException("H2_PASSWORD no definida en .env")
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
