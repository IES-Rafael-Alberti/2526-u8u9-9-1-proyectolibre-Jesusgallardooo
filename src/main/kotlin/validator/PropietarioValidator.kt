package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.Propietario

class PropietarioValidator {

    fun validate(entity: Propietario) {
        val errors = mutableListOf<String>()

        if (entity.nombre.isBlank()) errors.add("El nombre no puede estar vacío")
        if (entity.apellido.isBlank()) errors.add("El apellido no puede estar vacío")
        if (entity.telefono.isBlank()) errors.add("El teléfono no puede estar vacío")
        else if (!Validators.isValidPhone(entity.telefono)) errors.add("El teléfono no tiene un formato válido")
        if (entity.email.isBlank()) errors.add("El email no puede estar vacío")
        else if (!Validators.isValidEmail(entity.email)) errors.add("El email no tiene un formato válido")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
