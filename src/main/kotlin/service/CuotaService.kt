// service/CuotaServiceDual.kt
package service

import model.Cuota
import model.Socio
import repository.Repository
import repository.mongo.MongoCuotaRepository
import validator.CuotaValidator
import exception.NotFoundException
import exception.ValidationException
import java.time.LocalDate
import java.time.YearMonth

class CuotaService(
    private val csvRepo: Repository<Cuota, Long>,
    private val mongoRepo: MongoCuotaRepository,
    private val socioRepo: Repository<Socio, Long>
) {

    fun registrarCuota(socioId: Long, importe: Double, fechaPago: LocalDate): Cuota {
        // Validar socio
        val socio = socioRepo.findById(socioId)
            ?: throw NotFoundException("Socio con ID $socioId no existe")

        CuotaValidator.validarCuota(socioId, importe, fechaPago)

        // Evitar duplicado de mes en CSV
        val mes = YearMonth.from(fechaPago)
        val cuotasExistentes = csvRepo.findAll().filter { it.socioId == socioId }
        if (cuotasExistentes.any { YearMonth.from(it.fechaPago) == mes }) {
            throw ValidationException("El socio ya pagó la cuota de ${mes.month} ${mes.year}")
        }

        val cuota = Cuota(0, socioId, importe, fechaPago)

        // 1. Guardar en CSV (genera ID)
        val csvCuota = csvRepo.create(cuota)

        // 2. Guardar en MongoDB con el mismo ID
        mongoRepo.create(csvCuota)

        return csvCuota
    }

    fun listarTodasLasCuotas(): List<Cuota> = csvRepo.findAll()

    fun listarCuotasPorSocio(socioId: Long): List<Cuota> =
        csvRepo.findAll().filter { it.socioId == socioId }

    fun obtenerTotalPagadoPorSocio(socioId: Long): Double =
        listarCuotasPorSocio(socioId).sumOf { it.importe }

    fun actualizarCuota(cuota: Cuota): Cuota {
        // Validar
        CuotaValidator.validarCuota(cuota.socioId, cuota.importe, cuota.fechaPago)

        // Actualizar CSV
        val csvActualizada = csvRepo.update(cuota)

        // Actualizar MongoDB (automático)
        mongoRepo.update(csvActualizada)

        return csvActualizada
    }

    fun eliminarCuota(id: Long): Boolean {
        val okCsv = csvRepo.delete(id)
        val okMongo = mongoRepo.delete(id)
        return okCsv && okMongo
    }
}