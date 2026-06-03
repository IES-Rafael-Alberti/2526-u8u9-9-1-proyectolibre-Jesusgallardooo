package org.iesra.service

import org.iesra.exception.EntityAlreadyExistsException
import org.iesra.exception.EntityNotFoundException
import org.iesra.exception.RepositoryException
import org.iesra.model.Propietario
import org.iesra.repository.interfaces.CrudRepository
import org.iesra.validator.PropietarioValidator

class PropietarioService(
    private val repo: CrudRepository<Propietario, Int>,
    private val validator: PropietarioValidator = PropietarioValidator()
) {
    fun findAll(): List<Propietario> = repo.findAll()

    fun findById(id: Int): Propietario =
        repo.findById(id) ?: throw EntityNotFoundException("Propietario con id $id no encontrado")

    fun save(entity: Propietario): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) != null) {
            throw EntityAlreadyExistsException("Propietario con id ${entity.id} ya existe")
        }
        return repo.save(entity)
    }

    fun update(entity: Propietario): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) == null) {
            throw EntityNotFoundException("Propietario con id ${entity.id} no encontrado")
        }
        return repo.update(entity)
    }

    fun delete(id: Int): Boolean {
        if (repo.findById(id) == null) {
            throw EntityNotFoundException("Propietario con id $id no encontrado")
        }
        return repo.delete(id)
    }
}
