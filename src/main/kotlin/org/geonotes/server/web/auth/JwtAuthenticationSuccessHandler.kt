package org.geonotes.server.web.auth

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


class JwtAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) = Unit
}
