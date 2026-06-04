package org.iesra.validator

object Validators {

    private val EMAIL_REGEX = Regex("^[\\w.%-]+@[\\w.%-]+\\.[a-zA-Z]{2,}$")
    private val PHONE_REGEX = Regex("^\\+?\\d{7,15}$")
    private val DNI_REGEX = Regex("^\\d{8}[A-Z]?$")

    fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)

    fun isValidPhone(phone: String): Boolean = PHONE_REGEX.matches(phone)

    fun isValidDni(dni: String): Boolean = DNI_REGEX.matches(dni)

    fun isNotBlank(value: String): Boolean = value.isNotBlank()

    fun isNonNegative(value: Number): Boolean = value.toLong() >= 0
}
