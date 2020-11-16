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
import org.geonotes.server.core.exceptions.*


@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [AuthenticationFailedException::class])
    fun handleAuthException(e: RuntimeException, request: WebRequest): ResponseEntity<Any?> {
        log.debug("Caught AuthenticationFailedException: ${e.message}")
        return handleExceptionInternal(e, e.message, HttpHeaders(), HttpStatus.UNAUTHORIZED, request)
    }

    @ExceptionHandler(value = [UserAlreadyExistsException::class])
    fun handleUserAlreadyExistsException(e: RuntimeException, request: WebRequest): ResponseEntity<Any?> {
        log.debug("Caught UserAlreadyExistsException: ${e.message}")
        return handleExceptionInternal(e, "Username is busy", HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleBadRequestException(e: RuntimeException, request: WebRequest): ResponseEntity<Any?> {
        log.debug("Caught BadRequestException: ${e.message}")
        return handleExceptionInternal(e, e.message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleException(e: RuntimeException, request: WebRequest): ResponseEntity<Any?> {
        log.warn("Uncaught exception; Message: ${e.message}")
        return handleExceptionInternal(e, "Username is busy", HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    private val log: Logger by logger()
}