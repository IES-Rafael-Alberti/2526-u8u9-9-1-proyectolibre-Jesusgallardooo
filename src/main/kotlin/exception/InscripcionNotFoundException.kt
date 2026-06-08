package exception

class InscripcionNotFoundException(id: Long) : NotFoundException("Inscripción con ID $id no encontrada")

