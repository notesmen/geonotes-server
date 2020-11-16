package org.geonotes.server.web.auth

import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

import org.geonotes.server.logger


@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.debug("Authentication failed. Message: ${authException.message}")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }

    private val logger: Logger by logger()
}