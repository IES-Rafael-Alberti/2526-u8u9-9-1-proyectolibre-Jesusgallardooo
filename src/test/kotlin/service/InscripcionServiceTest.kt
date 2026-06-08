package service

import exception.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeTrue
import model.Actividad
import model.Socio
import repository.*
import java.time.LocalDate

class InscripcionServiceTest : FunSpec({
    lateinit var csvRepo: InMemoryInscripcionRepository
    lateinit var sqlRepo: InMemoryInscripcionRepository
    lateinit var socioRepo: InMemorySocioRepository
    lateinit var actividadRepo: InMemoryActividadRepository
    lateinit var service: InscripcionService

    var socioId = 0L
    var actividadId = 0L

    beforeEach {
        csvRepo = InMemoryInscripcionRepository()
        sqlRepo = InMemoryInscripcionRepository()
        socioRepo = InMemorySocioRepository()
        actividadRepo = InMemoryActividadRepository()
        socioId = socioRepo.create(Socio(0, "Juan", "García", "juan@mail.com", "633809570", true)).id
        actividadId = actividadRepo.create(Actividad(0, "Yoga", 15)).id
        service = InscripcionService(csvRepo, sqlRepo, socioRepo, actividadRepo)
    }

    test("inscripcion valida") {
        val insc = service.inscribirSocio(socioId, actividadId, LocalDate.now())
        insc.socioId shouldBe socioId
        insc.actividadId shouldBe actividadId
    }
    test("inscribir socio inexistente lanza SocioNotFoundException") {
        shouldThrow<SocioNotFoundException> { service.inscribirSocio(999L, actividadId, LocalDate.now()) }
    }
    test("inscribir en actividad inexistente lanza ActividadNotFoundException") {
        shouldThrow<ActividadNotFoundException> { service.inscribirSocio(socioId, 999L, LocalDate.now()) }
    }
    test("inscribir socio inactivo lanza SocioInactivoException") {
        socioRepo.update(socioRepo.findById(socioId)!!.copy(activo = false))
        shouldThrow<SocioInactivoException> { service.inscribirSocio(socioId, actividadId, LocalDate.now()) }
    }
    test("inscribir socio ya inscrito lanza SocioYaInscritoException") {
        service.inscribirSocio(socioId, actividadId, LocalDate.now())
        shouldThrow<SocioYaInscritoException> { service.inscribirSocio(socioId, actividadId, LocalDate.now()) }
    }
    test("inscribir sin plazas lanza ActividadSinPlazasException") {
        val act1plaza = actividadRepo.create(Actividad(0, "CrossFit", 1))
        service.inscribirSocio(socioId, act1plaza.id, LocalDate.now())
        val otroSocio = socioRepo.create(Socio(0, "Ana", "López", "ana@mail.com", "655123456", true))
        shouldThrow<ActividadSinPlazasException> { service.inscribirSocio(otroSocio.id, act1plaza.id, LocalDate.now()) }
    }
    test("cancelar inscripcion") {
        val insc = service.inscribirSocio(socioId, actividadId, LocalDate.now())
        service.cancelarInscripcion(insc.id).shouldBeTrue()
        service.listarTodasLasInscripciones().size shouldBe 0
    }
})
