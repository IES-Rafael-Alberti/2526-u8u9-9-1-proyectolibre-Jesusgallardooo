package service

import model.Cuota
import model.Socio
import repository.Repository
import validator.CuotaValidator
import exception.NotFoundException
import exception.ValidationException
import java.time.LocalDate
import java.time.YearMonth

/**
 * Servicio que gestiona la lógica de negocio de las cuotas.
 * Persiste simultáneamente en CSV y en MongoDB.
 * Evita duplicados de cuota en el mismo mes para un mismo socio.
 */
class CuotaService(
    private val csvRepo: Repository<Cuota, Long>,
    private val mongoRepo: Repository<Cuota, Long>,
    private val socioRepo: Repository<Socio, Long>
) {

    fun registrarCuota(socioId: Long, importe: Double, fechaPago: LocalDate): Cuota {
        val socio = socioRepo.findById(socioId)
            ?: throw NotFoundException("Socio con ID $socioId no existe")
        CuotaValidator.validarCuota(socioId, importe, fechaPago)
        val mes = YearMonth.from(fechaPago)
        val cuotasExistentes = csvRepo.findAll().filter { it.socioId == socioId }
        if (cuotasExistentes.any { YearMonth.from(it.fechaPago) == mes }) {
            throw ValidationException("El socio ya pagó la cuota de ${mes.month} ${mes.year}")
        }
        val cuota = Cuota(0, socioId, importe, fechaPago)
        val csvCuota = csvRepo.create(cuota)
        mongoRepo.create(csvCuota)
        return csvCuota
    }

    fun listarTodasLasCuotas(): List<Cuota> = csvRepo.findAll()
    fun listarCuotasPorSocio(socioId: Long): List<Cuota> = csvRepo.findAll().filter { it.socioId == socioId }
    fun obtenerTotalPagadoPorSocio(socioId: Long): Double = listarCuotasPorSocio(socioId).sumOf { it.importe }

    fun actualizarCuota(cuota: Cuota): Cuota {
        CuotaValidator.validarCuota(cuota.socioId, cuota.importe, cuota.fechaPago)
        val csvActualizada = csvRepo.update(cuota)
        mongoRepo.update(csvActualizada)
        return csvActualizada
    }

    fun eliminarCuota(id: Long): Boolean {
        val okCsv = csvRepo.delete(id)
        val okMongo = mongoRepo.delete(id)
        return okCsv && okMongo
    }
}