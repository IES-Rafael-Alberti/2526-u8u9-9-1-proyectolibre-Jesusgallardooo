package org.iesra.service

import org.iesra.exception.EntityAlreadyExistsException
import org.iesra.exception.EntityNotFoundException
import org.iesra.model.Veterinario
import org.iesra.repository.interfaces.CrudRepository
import org.iesra.validator.VeterinarioValidator

class VeterinarioService(
    private val repo: CrudRepository<Veterinario, Int>,
    private val validator: VeterinarioValidator = VeterinarioValidator()
) {
    fun findAll(): List<Veterinario> = repo.findAll()

    fun findById(id: Int): Veterinario =
        repo.findById(id) ?: throw EntityNotFoundException("Veterinario con id $id no encontrado")

    fun save(entity: Veterinario): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) != null) {
            throw EntityAlreadyExistsException("Veterinario con id ${entity.id} ya existe")
        }
        return repo.save(entity)
    }

    fun update(entity: Veterinario): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) == null) {
            throw EntityNotFoundException("Veterinario con id ${entity.id} no encontrado")
        }
        return repo.update(entity)
    }

    fun delete(id: Int): Boolean {
        if (repo.findById(id) == null) {
            throw EntityNotFoundException("Veterinario con id $id no encontrado")
        }
        return repo.delete(id)
    }
}
