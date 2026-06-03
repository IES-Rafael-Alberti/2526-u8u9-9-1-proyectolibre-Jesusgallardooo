package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.Cita

class CitaValidator {

    fun validate(entity: Cita) {
        val errors = mutableListOf<String>()

        if (entity.idMascota <= 0) errors.add("El id de la mascota no es válido")
        if (entity.idVeterinario <= 0) errors.add("El id del veterinario no es válido")
        if (entity.motivo.isBlank()) errors.add("El motivo no puede estar vacío")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
