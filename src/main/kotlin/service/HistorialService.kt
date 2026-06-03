package org.iesra.service

import org.iesra.exception.EntityAlreadyExistsException
import org.iesra.exception.EntityNotFoundException
import org.iesra.model.HistorialMedico
import org.iesra.model.Mascota
import org.iesra.repository.interfaces.CrudRepository
import org.iesra.repository.mongo.HistorialRepositoryMongo
import org.iesra.validator.HistorialMedicoValidator

class HistorialService(
    private val repo: HistorialRepositoryMongo,
    private val mascotaRepo: CrudRepository<Mascota, Int>,
    private val validator: HistorialMedicoValidator = HistorialMedicoValidator()
) {
    fun findAll(): List<HistorialMedico> = repo.findAll()

    fun findByMascotaId(mascotaId: Int): List<HistorialMedico> = repo.findByMascotaId(mascotaId)

    fun findById(id: String): HistorialMedico =
        repo.findById(id) ?: throw EntityNotFoundException("Historial con id $id no encontrado")

    fun save(entity: HistorialMedico): HistorialMedico? {
        if (entity.id != null) {
            throw EntityAlreadyExistsException("Historial con id ${entity.id} ya existe")
        }
        validator.validate(entity)
        if (mascotaRepo.findById(entity.idMascota) == null) {
            throw EntityNotFoundException("Mascota con id ${entity.idMascota} no encontrada")
        }
        return repo.save(entity)
    }

    fun update(entity: HistorialMedico): Boolean {
        validator.validate(entity)
        if (entity.id == null || repo.findById(entity.id) == null) {
            throw EntityNotFoundException("Historial con id ${entity.id} no encontrado")
        }
        return repo.update(entity)
    }

    fun delete(id: String): Boolean {
        if (repo.findById(id) == null) {
            throw EntityNotFoundException("Historial con id $id no encontrado")
        }
        return repo.delete(id)
    }
}
