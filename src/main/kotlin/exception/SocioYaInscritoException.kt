package exception

// exception/SocioYaInscritoException.kt

class SocioYaInscritoException(socioId: Long, actividadId: Long) :
    Exception("El socio $socioId ya está inscrito en la actividad $actividadId")