package com.noovoweb.project.exception

import com.noovoweb.project.response.ErrorsResponse
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
 * Application exception handler for specific non-validation exceptions.
 * ValidationException is handled by kotlin-validator-spring-mvc module's ValidationExceptionHandler.
 * Other unhandled exceptions will be handled by Spring's default error handler.
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