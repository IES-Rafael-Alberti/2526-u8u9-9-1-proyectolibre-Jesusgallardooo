package validator

import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate

class CuotaValidatorTest : FunSpec({
    test("importe valido") { CuotaValidator.validarImporte(50.0).shouldBeTrue() }
    test("importe invalido") { CuotaValidator.validarImporte(0.0).shouldBeFalse() }
    test("fecha pasada valida") { CuotaValidator.validarFecha(LocalDate.now().minusDays(1)).shouldBeTrue() }
    test("fecha futura invalida") { CuotaValidator.validarFecha(LocalDate.now().plusDays(1)).shouldBeFalse() }
    test("validacion completa lanza excepcion") {
        shouldThrow<ValidationException> { CuotaValidator.validarCuota(1L, -5.0, LocalDate.now()) }
    }
})
