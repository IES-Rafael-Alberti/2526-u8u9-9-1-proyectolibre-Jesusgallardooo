package repository

interface Repository<T> {
    fun findAll(): List<T>
    fun findById(id: Long): T?
    fun save(entity: T)
    fun delete(id: Long)
}