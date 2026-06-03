package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.Propietario

class PropietarioValidator {

    fun validate(entity: Propietario) {
        val errors = mutableListOf<String>()

        if (entity.nombre.isBlank()) errors.add("El nombre no puede estar vacío")
        if (entity.apellido.isBlank()) errors.add("El apellido no puede estar vacío")
        if (entity.telefono.isBlank()) errors.add("El teléfono no puede estar vacío")
        if (entity.email.isBlank()) errors.add("El email no puede estar vacío")
        if (!entity.email.contains("@")) errors.add("El email debe contener @")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
