package exception

class ActividadNotFoundException(id: Long) : NotFoundException("Actividad con ID $id no encontrada")

