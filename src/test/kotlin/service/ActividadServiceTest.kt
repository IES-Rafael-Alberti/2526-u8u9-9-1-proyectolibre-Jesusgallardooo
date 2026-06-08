package service

import exception.NotFoundException
import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeTrue
import model.Actividad
import repository.InMemoryActividadRepository

class ActividadServiceTest : FunSpec({
    lateinit var csvRepo: InMemoryActividadRepository
    lateinit var sqlRepo: InMemoryActividadRepository
    lateinit var service: ActividadService

    beforeEach {
        csvRepo = InMemoryActividadRepository()
        sqlRepo = InMemoryActividadRepository()
        service = ActividadService(csvRepo, sqlRepo)
    }

    test("crear actividad valida") {
        val actividad = service.crearActividad("Spinning", 20)
        actividad.nombre shouldBe "Spinning"
        actividad.plazasMaximas shouldBe 20
    }
    test("crear actividad invalida lanza ValidationException") {
        shouldThrow<ValidationException> { service.crearActividad("AB", 20) }
    }
    test("buscar actividad inexistente lanza NotFoundException") {
        shouldThrow<NotFoundException> { service.obtenerActividad(999L) }
    }
    test("CRUD completo") {
        val a = service.crearActividad("Yoga", 15)
        service.actualizarActividad(a.copy(nombre = "Yoga Avanzado")).nombre shouldBe "Yoga Avanzado"
        service.eliminarActividad(a.id).shouldBeTrue()
        service.listarTodasLasActividades().size shouldBe 0
    }
})
