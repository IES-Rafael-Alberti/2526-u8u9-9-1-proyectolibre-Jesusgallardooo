package validator

import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class EntrenadorValidatorTest : FunSpec({
    test("email valido") { EntrenadorValidator.validarEmail("trainer@gym.com").shouldBeTrue() }
    test("email invalido") { EntrenadorValidator.validarEmail("trainer").shouldBeFalse() }
    test("nombre valido") { EntrenadorValidator.validarNombre("Carlos").shouldBeTrue() }
    test("nombre invalido") { EntrenadorValidator.validarNombre("A").shouldBeFalse() }
    test("validacion completa lanza excepcion") {
        shouldThrow<ValidationException> { EntrenadorValidator.validarEntrenador("C", "carlos@gym.com", "Musculación") }
    }
})
