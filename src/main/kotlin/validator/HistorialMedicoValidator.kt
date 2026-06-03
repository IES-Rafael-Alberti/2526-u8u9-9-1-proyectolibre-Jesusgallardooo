package org.iesra.validator

import org.iesra.exception.ValidationException
import org.iesra.model.HistorialMedico

class HistorialMedicoValidator {

    fun validate(entity: HistorialMedico) {
        val errors = mutableListOf<String>()

        if (entity.idMascota <= 0) errors.add("El id de la mascota no es válido")
        if (entity.descripcion.isBlank()) errors.add("La descripción no puede estar vacía")
        if (entity.diagnostico.isBlank()) errors.add("El diagnóstico no puede estar vacío")
        if (entity.tratamiento.isBlank()) errors.add("El tratamiento no puede estar vacío")

        if (errors.isNotEmpty()) throw ValidationException(errors.joinToString("; "))
    }
}
