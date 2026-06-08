package exception

class EntrenadorNotFoundException(id: Long) : NotFoundException("Entrenador con ID $id no encontrado")

