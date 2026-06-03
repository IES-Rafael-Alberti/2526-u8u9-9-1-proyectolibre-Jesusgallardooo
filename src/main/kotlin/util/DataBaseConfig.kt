package org.iesra.util

data class DataBaseConfig(
    val url: String = "jdbc:h2:./data/vet_manager",
    val user: String = "jesus",
    val password: String = "jesus"
)
