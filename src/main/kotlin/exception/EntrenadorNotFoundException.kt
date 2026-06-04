package exception

// exception/EntrenadorNotFoundException.kt

class EntrenadorNotFoundException(id: Long) : NotFoundException("Entrenador con ID $id no encontrado")