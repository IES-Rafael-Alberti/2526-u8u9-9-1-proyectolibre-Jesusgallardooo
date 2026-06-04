package repository

interface Repository<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun update(entity: T): T
    fun create(entity: T): T
    fun delete(id: ID): Boolean
}