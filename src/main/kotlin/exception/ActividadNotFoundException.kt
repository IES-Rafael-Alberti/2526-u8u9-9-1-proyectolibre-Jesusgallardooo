package exception

// exception/ActividadNotFoundException.kt

class ActividadNotFoundException(id: Long) : NotFoundException("Actividad con ID $id no encontrada")