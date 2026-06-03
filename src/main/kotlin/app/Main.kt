package app

import org.iesra.model.HistorialMedico
import org.iesra.repository.mongo.HistorialRepositoryMongo
import java.util.Date

fun main() {
    val repo = HistorialRepositoryMongo()

    val historial = HistorialMedico(
        id = null,
        idMascota = 1,
        descripcion = "Revision general - todo correcto",
        diagnostico = "nada",
        tratamiento = "Ninguno",
        fecha = Date()
    )

    val insertado = repo.save(historial)
    if (insertado != null) {
        println("INSERT MONGO exitoso: ${insertado.id}")
    } else {
        println("INSERT MONGO fallo (ver logs)")
    }

    val lista = repo.findByMascotaId(1)
    println("LISTA MONGO (Mascota 1): ${lista.size} registros")
    lista.forEach { println(it) }

    var lista2 = repo.findByMascotaId(1)
    println("CONSULTA REPETIDA: ${lista2.size} registros")



    println("FIN: La aplicacion no se detuvo por errores de MongoDB.")
}
