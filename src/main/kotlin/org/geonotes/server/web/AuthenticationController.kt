package org.geonotes.server.web

import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import org.geonotes.server.logger
import org.geonotes.server.core.UsersManager
import org.geonotes.server.core.exceptions.BadRequestException


@RestController
@RequestMapping("/api/auth")
class AuthenticationController {
    @PostMapping("register")
    fun registerUser(@RequestBody request: RegistrationRequest): ResponseEntity<Unit> {
        if (request.username.length !in minUsernameLength..maxUsernameLength) {
            throw BadRequestException("username.length must be in [4, 20]")
        }

        if (request.password.length !in minPasswordLength..maxPasswordLength) {
            throw BadRequestException("password.length must be in [6, 24]")
        }

        with(Regex(emailRegexPattern)) {
            if (!matches(request.email)) {
                throw BadRequestException("email doesn't match a pattern")
            }
        }

        usersManager.registerUser(request)

        return ResponseEntity.ok(Unit)
    }

    @PostMapping("login")
    fun login(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        if (request.username.length !in minUsernameLength..maxUsernameLength) {
            throw BadRequestException("username.length must be in [4, 20]")
        }

        if (request.password.length !in minPasswordLength..maxPasswordLength) {
            throw BadRequestException("password.length must be in [6, 24]")
        }

        val token: String = usersManager.startNewSession(request)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @Autowired
    private lateinit var usersManager: UsersManager

    @Value("\${api.min-username-length}")
    private var minUsernameLength: Int = 0

    @Value("\${api.max-username-length}")
    private var maxUsernameLength: Int = 0

    @Value("\${api.min-password-length}")
    private var minPasswordLength: Int = 0

    @Value("\${api.max-password-length}")
    private var maxPasswordLength: Int = 0

    private val emailRegexPattern: String = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

    private val logger: Logger by logger()
}