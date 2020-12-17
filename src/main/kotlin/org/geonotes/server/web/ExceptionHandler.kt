package org.geonotes.server.web

import java.lang.RuntimeException

import org.slf4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import org.geonotes.server.logger


@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleException(e: RuntimeException, request: WebRequest): ResponseEntity<Any?> {
        log.warn("Uncaught exception; Message: ${e.message}")
        return handleExceptionInternal(e, "Internal error", HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    private val log: Logger by logger()
}
