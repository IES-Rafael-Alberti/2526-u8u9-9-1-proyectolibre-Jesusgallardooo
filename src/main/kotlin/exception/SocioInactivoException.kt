package exception

// exception/SocioInactivoException.kt

class SocioInactivoException(socioId: Long) :
    Exception("El socio $socioId está inactivo y no puede realizar esta operación")