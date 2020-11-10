package org.geonotes.server.web

import org.geonotes.server.core.TokenRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired

import org.geonotes.server.core.UserRepository
import org.geonotes.server.core.UsersManager
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api")
class AuthenticationController {
    @PostMapping("register")
    fun registerUser(@RequestBody request: RegistrationRequest): ResponseEntity<Unit> {
        usersManager.registerUser(request)
        return ResponseEntity.ok(Unit)
    }

    @PostMapping("login")
    fun login(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val token: String = usersManager.startNewSession(request)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @Autowired
    private lateinit var usersManager: UsersManager
}