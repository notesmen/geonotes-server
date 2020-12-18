package org.geonotes.server.web.auth

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

import org.geonotes.server.logger
import org.geonotes.server.core.model.User
import org.geonotes.server.core.UserRepository


@Component
class JwtAuthenticationFilter : AbstractAuthenticationProcessingFilter("/api/*") {
    init {
        setAuthenticationSuccessHandler(JwtAuthenticationSuccessHandler())
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val header: String? = request!!.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer ")) {
            throw AuthenticationServiceException("No JWT token found in request headers")
        }

        val token = header.substring(7)
        val tokenInfo: TokenHandler.TokenInfo? = tokenHandler.extractTokenInfo(token) ?:
            throw AuthenticationServiceException("Invalid token")

        val tokenOwner: User? = userRepository.findUserByUsername(tokenInfo!!.username)

        if (tokenOwner == null) {
            log.warn("Token is valid, but owner not found; username: '${tokenInfo.username}'")
            throw AuthenticationServiceException("Token owner not found")
        }

        return JwtTokenAuthentication(tokenInfo, true)
    }

    @Autowired
    override fun setAuthenticationManager(authenticationManager: AuthenticationManager) {
        super.setAuthenticationManager(authenticationManager)
    }

    @Autowired
    private lateinit var tokenHandler: TokenHandler

    @Autowired
    private lateinit var userRepository: UserRepository

    private val log: Logger by logger()
}
