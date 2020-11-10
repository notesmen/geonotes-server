package org.geonotes.server.core

import java.util.Optional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import org.geonotes.server.core.exceptions.UserAlreadyExists
import org.geonotes.server.core.exceptions.UserNotFound
import org.geonotes.server.web.AuthenticationRequest
import org.geonotes.server.web.RegistrationRequest
import org.geonotes.server.core.exceptions.AuthenticationFailed


@Component
class UsersManager {
    fun registerUser(request: RegistrationRequest) {
        if (userRepository.existsByUsername(request.username)) {
            throw UserAlreadyExists(request.username)
        }

    }

    fun startNewSession(request: AuthenticationRequest): String {
        val user: Optional<User> = userRepository.findUserByUsername(request.username)
        if (!user.isPresent) {
            throw UserNotFound(request.username)
        }

        val passwordHash: String = passwordEncoder.encode(request.password)
        if (user.get().passwordHash != passwordHash) {
            throw AuthenticationFailed()
        }

//        tokenRepository.save()

        return ""
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
}