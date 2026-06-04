import java.time.LocalDate

data class Inscripción(
    val id: Long,
    val socioId: Long,
    val actividadId: Long,
    val fechaInscricao: LocalDate
)