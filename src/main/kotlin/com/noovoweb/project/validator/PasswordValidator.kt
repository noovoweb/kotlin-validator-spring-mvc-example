package com.noovoweb.project.validator

import com.noovoweb.validator.CustomValidator
import com.noovoweb.validator.ValidationContext


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@CustomValidator(
    validator = "com.noovoweb.project.validator.PasswordValidator::validateStrongPassword",
    message = "password.strong_password"
)
annotation class StrongPassword

/**
 * Custom Password validators.
 */
object PasswordValidator {
    /**
     * Validates that a password meets strong password requirements:
     * - Minimum 12 characters
     * - At least one uppercase letter (A-Z)
     * - At least one lowercase letter (a-z)
     * - At least one digit (0-9)
     * - At least one special character
     */
    suspend fun validateStrongPassword(value: String?, context: ValidationContext): Boolean {
        if (value == null) return true

        val hasMinLength = value.length >= 12
        val hasUppercase = value.any { it.isUpperCase() }
        val hasLowercase = value.any { it.isLowerCase() }
        val hasDigit = value.any { it.isDigit() }
        val hasSpecialChar = value.any { !it.isLetterOrDigit() }

        return hasMinLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar
    }
}
