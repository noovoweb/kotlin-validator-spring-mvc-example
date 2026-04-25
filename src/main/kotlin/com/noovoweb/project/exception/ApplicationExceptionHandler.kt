package com.noovoweb.project.exception

import com.noovoweb.project.response.ErrorsResponse
import com.noovoweb.validator.ValidationException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.LocaleResolver
import java.util.*

/**
 * Application exception handler for all exceptions.
 */
@ControllerAdvice
class ApplicationExceptionHandler(
    private val messageSource: MessageSource,
    private val localeResolver: LocaleResolver,
    @Value("\${spring.profiles.active:local}") private val activeProfile: String
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private fun isProduction() = activeProfile == "prod" || activeProfile == "production"

    private fun generateCorrelationId() = UUID.randomUUID().toString()

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException, request: HttpServletRequest): ResponseEntity<ErrorsResponse> {
        logger.warn { "Validation failed: ${e.errors}" }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorsResponse(message = "Validation Failed", errors = e.errors))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorsResponse> {
        val correlationId = generateCorrelationId()
        val locale = localeResolver.resolveLocale(request)

        logger.warn(e) { "[$correlationId] HttpMessageNotReadableException: ${e.message}" }

        val message = if (isProduction()) {
            messageSource.getMessage("error.bad_request", null, locale)
        } else {
            e.message ?: "Invalid request"
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorsResponse(message = message, errors = mapOf("general" to listOf(message))))
    }

}