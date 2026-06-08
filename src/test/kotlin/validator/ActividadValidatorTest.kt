package validator

import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class ActividadValidatorTest : FunSpec({
    test("nombre valido") { ActividadValidator.validarNombre("Spinning").shouldBeTrue() }
    test("nombre invalido") { ActividadValidator.validarNombre("AB").shouldBeFalse() }
    test("plazas en rango") { ActividadValidator.validarPlazas(20).shouldBeTrue() }
    test("plazas fuera de rango") { ActividadValidator.validarPlazas(0).shouldBeFalse() }
    test("validacion completa lanza excepcion") {
        shouldThrow<ValidationException> { ActividadValidator.validarActividad("AB", 20) }
    }
})
