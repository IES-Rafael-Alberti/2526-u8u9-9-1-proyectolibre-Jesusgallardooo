package service
// service/CuotaService.kt

import model.Cuota
import model.Socio
import repository.Repository
import validator.CuotaValidator
import java.time.LocalDate
import java.time.YearMonth

class CuotaService(
    private val cuotaRepository: Repository<Cuota, Long>,
    private val socioRepository: Repository<Socio, Long>
) {

    fun registrarCuota(socioId: Long, importe: Double, fechaPago: LocalDate): Cuota {
        // Verificar que el socio existe
        val socio = socioRepository.findById(socioId)
            ?: throw NotFoundException("Socio con ID $socioId no encontrado")

        // Validar cuota
        try {
            CuotaValidator.validarCuota(socioId, importe, fechaPago)
        } catch (e: ValidationException) {
            throw ValidationException("Error al registrar cuota: ${e.message}")
        }

        // Verificar que no haya pagado ya este mes
        val mes = YearMonth.from(fechaPago)
        val yaPagada = cuotaRepository.findAll().any {
            it.socioId == socioId && YearMonth.from(it.fechaPago) == mes
        }

        if (yaPagada) {
            throw ValidationException("El socio ya pagó la cuota de ${mes.month} ${mes.year}")
        }

        val cuota = Cuota(
            id = 0,
            socioId = socioId,
            importe = importe,
            fechaPago = fechaPago
        )

        return cuotaRepository.create(cuota)
    }

    fun obtenerCuota(id: Long): Cuota {
        return cuotaRepository.findById(id)
            ?: throw NotFoundException("Cuota con ID $id no encontrada")
    }

    fun listarTodasLasCuotas(): List<Cuota> {
        return cuotaRepository.findAll()
    }

    fun listarCuotasPorSocio(socioId: Long): List<Cuota> {
        return cuotaRepository.findAll().filter { it.socioId == socioId }
    }

    fun listarCuotasPorMes(year: Int, month: Int): List<Cuota> {
        return cuotaRepository.findAll().filter {
            it.fechaPago.year == year && it.fechaPago.monthValue == month
        }
    }

    fun obtenerTotalPagadoPorSocio(socioId: Long): Double {
        return listarCuotasPorSocio(socioId).sumOf { it.importe }
    }

    fun eliminarCuota(id: Long): Boolean {
        obtenerCuota(id)
        return cuotaRepository.delete(id)
    }
}