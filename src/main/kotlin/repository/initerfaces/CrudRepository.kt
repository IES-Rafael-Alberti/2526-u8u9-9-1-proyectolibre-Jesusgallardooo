package org.iesra.repository.initerfaces

interface CrudRepository<T, ID>{

    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(entity: T): Boolean
    fun update(entity: T): Boolean
    fun delete(id: ID): Boolean

}