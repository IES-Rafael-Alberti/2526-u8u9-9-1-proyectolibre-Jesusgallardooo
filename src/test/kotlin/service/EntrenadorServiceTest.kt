package service

import exception.NotFoundException
import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeTrue
import repository.InMemoryEntrenadorRepository

class EntrenadorServiceTest : FunSpec({
    lateinit var csvRepo: InMemoryEntrenadorRepository
    lateinit var sqlRepo: InMemoryEntrenadorRepository
    lateinit var service: EntrenadorService

    beforeEach {
        csvRepo = InMemoryEntrenadorRepository()
        sqlRepo = InMemoryEntrenadorRepository()
        service = EntrenadorService(csvRepo, sqlRepo)
    }

    test("crear entrenador valido") {
        val e = service.crearEntrenador("Carlos", "carlos@gym.com", "Musculación")
        e.nombre shouldBe "Carlos"
        e.especialidad shouldBe "Musculación"
    }
    test("crear entrenador invalido lanza ValidationException") {
        shouldThrow<ValidationException> { service.crearEntrenador("C", "carlos@gym.com", "Musculación") }
    }
    test("buscar entrenador inexistente lanza NotFoundException") {
        shouldThrow<NotFoundException> { service.obtenerEntrenador(999L) }
    }
    test("CRUD completo") {
        val e = service.crearEntrenador("Ana", "ana@gym.com", "Yoga")
        service.actualizarEntrenador(e.copy(especialidad = "Pilates")).especialidad shouldBe "Pilates"
        service.eliminarEntrenador(e.id).shouldBeTrue()
        service.listarTodosLosEntrenadores().size shouldBe 0
    }
})
