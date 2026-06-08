package service

import exception.NotFoundException
import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeTrue
import model.Socio
import repository.InMemoryCuotaRepository
import repository.InMemorySocioRepository
import java.time.LocalDate

class CuotaServiceTest : FunSpec({
    lateinit var csvRepo: InMemoryCuotaRepository
    lateinit var mongoRepo: InMemoryCuotaRepository
    lateinit var socioRepo: InMemorySocioRepository
    lateinit var service: CuotaService

    beforeEach {
        csvRepo = InMemoryCuotaRepository()
        mongoRepo = InMemoryCuotaRepository()
        socioRepo = InMemorySocioRepository()
        service = CuotaService(csvRepo, mongoRepo, socioRepo)
    }

    fun crearSocio(): Long = socioRepo.create(Socio(0, "Juan", "García", "juan@mail.com", "633809570", true)).id

    test("registrar cuota valida") {
        val cuota = service.registrarCuota(crearSocio(), 50.0, LocalDate.now())
        cuota.importe shouldBe 50.0
    }
    test("registrar cuota con socio inexistente lanza NotFoundException") {
        shouldThrow<NotFoundException> { service.registrarCuota(999L, 50.0, LocalDate.now()) }
    }
    test("registrar cuota con importe invalido lanza ValidationException") {
        shouldThrow<ValidationException> { service.registrarCuota(crearSocio(), -50.0, LocalDate.now()) }
    }
    test("no permite duplicado de mes para el mismo socio") {
        val socioId = crearSocio()
        service.registrarCuota(socioId, 50.0, LocalDate.of(2026, 1, 15))
        shouldThrow<ValidationException> { service.registrarCuota(socioId, 50.0, LocalDate.of(2026, 1, 20)) }
    }
    test("listar cuotas por socio y total pagado") {
        val socioId = crearSocio()
        service.registrarCuota(socioId, 50.0, LocalDate.now().minusMonths(2))
        service.registrarCuota(socioId, 75.0, LocalDate.now().minusMonths(1))
        service.listarCuotasPorSocio(socioId).size shouldBe 2
        service.obtenerTotalPagadoPorSocio(socioId) shouldBe 125.0
    }
    test("actualizar y eliminar cuota") {
        val cuota = service.registrarCuota(crearSocio(), 50.0, LocalDate.now())
        service.actualizarCuota(cuota.copy(importe = 75.0)).importe shouldBe 75.0
        service.eliminarCuota(cuota.id).shouldBeTrue()
    }
})
