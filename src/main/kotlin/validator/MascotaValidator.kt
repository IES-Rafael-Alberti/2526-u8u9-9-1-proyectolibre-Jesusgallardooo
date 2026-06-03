package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.Mascota

class MascotaValidator {

    fun validate(entity: Mascota) {
        val errors = mutableListOf<String>()

        if (entity.nombre.isBlank()) errors.add("El nombre no puede estar vacío")
        if (entity.especie.isBlank()) errors.add("La especie no puede estar vacía")
        if (entity.raza.isBlank()) errors.add("La raza no puede estar vacía")
        if (entity.edad < 0) errors.add("La edad no puede ser negativa")
        if (entity.idPropietario <= 0) errors.add("El id del propietario no es válido")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
