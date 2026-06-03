package app

import org.iesra.model.Mascota
import org.iesra.model.Propietario
import org.iesra.repository.sql.DataBaseManager
import org.iesra.repository.sql.MascotaRepositorySql
import org.iesra.repository.sql.PropietarioRepositorySql

fun main() {

    val manager = DataBaseManager()

    val propietarioRepo = PropietarioRepositorySql(manager)
    val mascotaRepo = MascotaRepositorySql(manager)

    // 🔵 1. Crear propietario primero (OBLIGATORIO por FK)
    val propietario = Propietario(
        id = 1,
        nombre = "Juan",
        apellido = "Pérez",
        telefono = "600123123",
        email = "juan@test.com"
    )

    println("INSERT PROPIETARIO: " + propietarioRepo.save(propietario))

    // 🔵 2. Crear mascota (ya con FK válida)
    val mascota = Mascota(
        id = 1,
        nombre = "Toby",
        especie = "Perro",
        raza = "Labrador",
        edad = 5,
        idPropietario = 1
    )

    println("INSERT MASCOTA: " + mascotaRepo.save(mascota))

    // 🔵 3. READ ALL
    println("LISTA: " + mascotaRepo.findAll())

    // 🔵 4. READ BY ID
    val encontrada = mascotaRepo.findById(1)
    println("FIND BY ID: $encontrada")

    // 🔵 5. UPDATE (IMPORTANTE: usar el objeto actualizado)
    val mascotaActualizada = mascota.copy(nombre = "Toby actualizado")
    println("UPDATE: " + mascotaRepo.update(mascotaActualizada))

    // 🔵 6. DELETE
    println("DELETE: " + mascotaRepo.delete(1))
}