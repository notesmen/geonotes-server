package org.geonotes.server.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired

import org.geonotes.server.core.UsersManager

@RestController
@RequestMapping("/api")
class AuthenticationController {
    @PostMapping("register")
    fun registerUser(@RequestBody request: RegistrationRequest): ResponseEntity<Unit> {
        usersManager.registerUser(
            RegistrationRequest(
                request.username,
                passwordEncoder.encode(request.password),
                request.email
            )
        )
        return ResponseEntity.ok(Unit)
    }

    @PostMapping("login")
    fun login(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val token: String = usersManager.startNewSession(
            AuthenticationRequest(
                request.username,
                passwordEncoder.encode(request.password)
            )
        )
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @Autowired
    private lateinit var usersManager: UsersManager

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
}