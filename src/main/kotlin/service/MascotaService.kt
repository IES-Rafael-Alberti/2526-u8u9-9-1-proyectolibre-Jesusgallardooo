package org.iesra.service

import org.iesra.exception.EntityAlreadyExistsException
import org.iesra.exception.EntityNotFoundException
import org.iesra.model.Mascota
import org.iesra.model.Propietario
import org.iesra.repository.interfaces.CrudRepository
import org.iesra.validator.MascotaValidator

class MascotaService(
    private val repo: CrudRepository<Mascota, Int>,
    private val propietarioRepo: CrudRepository<Propietario, Int>,
    private val validator: MascotaValidator = MascotaValidator()
) {
    fun findAll(): List<Mascota> = repo.findAll()

    fun findById(id: Int): Mascota =
        repo.findById(id) ?: throw EntityNotFoundException("Mascota con id $id no encontrada")

    fun save(entity: Mascota): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) != null) {
            throw EntityAlreadyExistsException("Mascota con id ${entity.id} ya existe")
        }
        if (propietarioRepo.findById(entity.idPropietario) == null) {
            throw EntityNotFoundException("Propietario con id ${entity.idPropietario} no encontrado")
        }
        return repo.save(entity)
    }

    fun update(entity: Mascota): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) == null) {
            throw EntityNotFoundException("Mascota con id ${entity.id} no encontrada")
        }
        if (propietarioRepo.findById(entity.idPropietario) == null) {
            throw EntityNotFoundException("Propietario con id ${entity.idPropietario} no encontrado")
        }
        return repo.update(entity)
    }

    fun delete(id: Int): Boolean {
        if (repo.findById(id) == null) {
            throw EntityNotFoundException("Mascota con id $id no encontrada")
        }
        return repo.delete(id)
    }
}
