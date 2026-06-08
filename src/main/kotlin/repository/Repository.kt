package repository

/**
 * Interfaz genérica para operaciones CRUD sobre cualquier tipo de entidad.
 * @param T Tipo de la entidad.
 * @param ID Tipo del identificador de la entidad.
 */
interface Repository<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun update(entity: T): T
    fun create(entity: T): T
    fun delete(id: ID): Boolean
}
