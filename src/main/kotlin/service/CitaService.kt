package org.iesra.service

import org.iesra.exception.EntityAlreadyExistsException
import org.iesra.exception.EntityNotFoundException
import org.iesra.model.Cita
import org.iesra.model.Mascota
import org.iesra.model.Veterinario
import org.iesra.repository.interfaces.CrudRepository
import org.iesra.validator.CitaValidator

class CitaService(
    private val repo: CrudRepository<Cita, Int>,
    private val mascotaRepo: CrudRepository<Mascota, Int>,
    private val veterinarioRepo: CrudRepository<Veterinario, Int>,
    private val validator: CitaValidator = CitaValidator()
) {
    fun findAll(): List<Cita> = repo.findAll()

    fun findById(id: Int): Cita =
        repo.findById(id) ?: throw EntityNotFoundException("Cita con id $id no encontrada")

    fun save(entity: Cita): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) != null) {
            throw EntityAlreadyExistsException("Cita con id ${entity.id} ya existe")
        }
        if (mascotaRepo.findById(entity.idMascota) == null) {
            throw EntityNotFoundException("Mascota con id ${entity.idMascota} no encontrada")
        }
        if (veterinarioRepo.findById(entity.idVeterinario) == null) {
            throw EntityNotFoundException("Veterinario con id ${entity.idVeterinario} no encontrado")
        }
        return repo.save(entity)
    }

    fun update(entity: Cita): Boolean {
        validator.validate(entity)
        if (repo.findById(entity.id) == null) {
            throw EntityNotFoundException("Cita con id ${entity.id} no encontrada")
        }
        if (mascotaRepo.findById(entity.idMascota) == null) {
            throw EntityNotFoundException("Mascota con id ${entity.idMascota} no encontrada")
        }
        if (veterinarioRepo.findById(entity.idVeterinario) == null) {
            throw EntityNotFoundException("Veterinario con id ${entity.idVeterinario} no encontrado")
        }
        return repo.update(entity)
    }

    fun delete(id: Int): Boolean {
        if (repo.findById(id) == null) {
            throw EntityNotFoundException("Cita con id $id no encontrada")
        }
        return repo.delete(id)
    }
}
