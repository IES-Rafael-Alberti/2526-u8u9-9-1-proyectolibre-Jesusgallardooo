package service

import exception.NotFoundException
import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import model.Socio
import repository.InMemorySocioRepository

class SocioServiceTest : FunSpec({
    lateinit var csvRepo: InMemorySocioRepository
    lateinit var sqlRepo: InMemorySocioRepository
    lateinit var service: SocioService

    beforeEach {
        csvRepo = InMemorySocioRepository()
        sqlRepo = InMemorySocioRepository()
        service = SocioService(csvRepo, sqlRepo)
    }

    test("crear socio valido") {
        val socio = service.crearSocio("Juan", "García", "juan@mail.com", "633809570")
        socio.nombre shouldBe "Juan"
        socio.activo shouldBe true
    }
    test("crear socio invalido lanza ValidationException") {
        shouldThrow<ValidationException> { service.crearSocio("J", "García", "juan@mail.com", "633809570") }
    }
    test("buscar socio inexistente lanza NotFoundException") {
        shouldThrow<NotFoundException> { service.obtenerSocio(999L) }
    }
    test("dar de baja y alta") {
        val socio = service.crearSocio("Juan", "García", "juan@mail.com", "633809570")
        service.darDeBaja(socio.id).activo shouldBe false
        service.darDeAlta(socio.id).activo shouldBe true
    }
    test("eliminar socio") {
        val socio = service.crearSocio("Juan", "García", "juan@mail.com", "633809570")
        service.eliminarSocio(socio.id).shouldBeTrue()
        service.eliminarSocio(999L).shouldBeFalse()
    }
    test("listar y contar") {
        service.crearSocio("Juan", "García", "juan@mail.com", "633809570")
        service.crearSocio("Ana", "López", "ana@mail.com", "655123456")
        service.listarTodosLosSocios().size shouldBe 2
        service.contarSocios() shouldBe 2
    }
})
