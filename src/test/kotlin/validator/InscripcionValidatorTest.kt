package validator

import exception.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate

class InscripcionValidatorTest : FunSpec({
    test("fecha hoy valida") { InscripcionValidator.validarFechas(LocalDate.now()).shouldBeTrue() }
    test("fecha futura invalida") { InscripcionValidator.validarFechas(LocalDate.now().plusDays(1)).shouldBeFalse() }
    test("validacion completa con fecha futura lanza excepcion") {
        shouldThrow<ValidationException> { InscripcionValidator.validarInscripcion(1L, 1L, LocalDate.now().plusDays(5)) }
    }
})
