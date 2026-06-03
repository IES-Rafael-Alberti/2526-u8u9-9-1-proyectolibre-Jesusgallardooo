package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.Veterinario

class VeterinarioValidator {

    fun validate(entity: Veterinario) {
        val errors = mutableListOf<String>()

        if (entity.nombre.isBlank()) errors.add("El nombre no puede estar vacío")
        if (entity.apellido.isBlank()) errors.add("El apellido no puede estar vacío")
        if (entity.especialidad.isBlank()) errors.add("La especialidad no puede estar vacía")
        if (entity.telefono.isBlank()) errors.add("El teléfono no puede estar vacío")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
