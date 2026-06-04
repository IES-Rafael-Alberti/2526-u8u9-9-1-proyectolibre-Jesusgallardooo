package exception

// exception/CuotaNotFoundException.kt

class CuotaNotFoundException(id: Long) : NotFoundException("Cuota con ID $id no encontrada")