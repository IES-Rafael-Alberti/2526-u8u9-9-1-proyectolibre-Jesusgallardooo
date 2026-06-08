package exception

class CuotaNotFoundException(id: Long) : NotFoundException("Cuota con ID $id no encontrada")

