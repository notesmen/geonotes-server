package org.geonotes.server.core

import org.bson.types.ObjectId
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import org.geonotes.server.logger
import org.geonotes.server.core.exceptions.UserAlreadyExistsException
import org.geonotes.server.core.exceptions.AuthenticationFailedException
import org.geonotes.server.web.AuthenticationRequest
import org.geonotes.server.web.RegistrationRequest
import org.geonotes.server.web.auth.TokenHandler


@Component
class UsersManager {
    fun registerUser(request: RegistrationRequest) {
        if (userRepository.existsByUsername(request.username)) {
            throw UserAlreadyExistsException(request.username)
        }

        val user = User(ObjectId.get(), request.username, passwordEncoder.encode(request.password), request.email)

        userRepository.save(user)
    }

    fun startNewSession(request: AuthenticationRequest): String {
        val user: User = userRepository.findUserByUsername(request.username)
            ?: throw AuthenticationFailedException(request.username)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw AuthenticationFailedException(request.username)
        }

        val token: String = tokenHandler.generateToken(user) ?:
            throw AuthenticationFailedException()
        logger.info("New session for '${user.username}' successfully created")
        return token
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenHandler: TokenHandler

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    private val logger: Logger by logger()
}
