package exception

class SocioInactivoException(socioId: Long) :
    Exception("El socio $socioId está inactivo y no puede realizar esta operación")

