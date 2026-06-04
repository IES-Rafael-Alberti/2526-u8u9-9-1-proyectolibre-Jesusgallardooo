package exception

// exception/InscripcionNotFoundException.kt

class InscripcionNotFoundException(id: Long) : NotFoundException("Inscripción con ID $id no encontrada")