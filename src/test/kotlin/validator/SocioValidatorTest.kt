package validator

import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class SocioValidatorTest : FunSpec({
    test("email valido") { SocioValidator.validarEmail("user@example.com").shouldBeTrue() }
    test("email invalido") { SocioValidator.validarEmail("userexample.com").shouldBeFalse() }
    test("telefono valido") { SocioValidator.validarTelefono("633809570").shouldBeTrue() }
    test("telefono invalido") { SocioValidator.validarTelefono("1234").shouldBeFalse() }
    test("nombre valido") { SocioValidator.validarNombre("Jesús").shouldBeTrue() }
    test("nombre invalido") { SocioValidator.validarNombre("A").shouldBeFalse() }
    test("validacion completa lanza excepcion") {
        shouldThrow<ValidationException> { SocioValidator.validarSocioCompleto("J", "García", "juan@mail.com", "633809570") }
    }
})
