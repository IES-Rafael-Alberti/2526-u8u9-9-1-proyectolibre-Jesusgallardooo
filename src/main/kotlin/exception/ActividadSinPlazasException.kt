package exception

// exception/ActividadSinPlazasException.kt

class ActividadSinPlazasException(actividadId: Long, plazasDisponibles: Int) :
    Exception("La actividad $actividadId no tiene plazas disponibles (disponibles: $plazasDisponibles)")