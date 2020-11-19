package org.geonotes.server.core

import org.bson.types.ObjectId
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import org.geonotes.server.logger
import org.geonotes.server.web.AuthenticationRequest
import org.geonotes.server.web.AuthenticationResponse
import org.geonotes.server.web.RegistrationRequest
import org.geonotes.server.web.auth.TokenHandler


@Component
class UsersManager {
    fun registerUser(request: RegistrationRequest): ResponseEntity<String> {
        if (userRepository.existsByUsername(request.username)) {
            return ResponseEntity("Username '${request.username}' is busy", HttpStatus.CONFLICT)
        }

        val user = User(ObjectId.get(), request.username, passwordEncoder.encode(request.password), request.email)

        userRepository.save(user)
        return ResponseEntity("Registration successful", HttpStatus.CREATED)
    }

    fun startNewSession(request: AuthenticationRequest): ResponseEntity<Any?> {
        val user: User = userRepository.findUserByUsername(request.username)
            ?: return ResponseEntity(
                "Authentication for user '${request.username}' failed. Wrong username/password",
                HttpStatus.BAD_REQUEST
            )

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity(
                "Authentication for user '${request.username}' failed. Wrong username/password",
                HttpStatus.BAD_REQUEST
            )
        }

        val token: String = tokenHandler.generateToken(user)
            ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        logger.info("New session for '${user.username}' successfully created")
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenHandler: TokenHandler

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    private val logger: Logger by logger()
}
