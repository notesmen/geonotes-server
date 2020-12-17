package org.geonotes.server.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import org.geonotes.server.core.UsersManager
import org.springframework.http.HttpStatus


@RestController
@RequestMapping("/api/auth")
class AuthenticationController {
    @PostMapping("register")
    fun registerUser(@RequestBody request: RegistrationRequest): ResponseEntity<String> {
        validateRequestDetails(request.username, request.password, request.email)?.let { error ->
            return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        }

        return usersManager.registerUser(request)
    }

    @PostMapping("login")
    fun login(@RequestBody request: AuthenticationRequest): ResponseEntity<Any?> {
        validateRequestDetails(request.username, request.password, null)?.let { error ->
            return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        }

        return usersManager.startNewSession(request)
    }

    private fun validateRequestDetails(username: String, password: String, email: String?): String? {
        if (!Regex(usernameRegexPattern).matches(username)) {
            return "username doesn't match a pattern $usernameRegexPattern"
        }

        if (!Regex(passwordRegexPattern).matches(password)) {
            return "password doesn't match a pattern $passwordRegexPattern"
        }

        if (email != null && !Regex(emailRegexPattern).matches(email)) {
            return "email doesn't match a pattern $emailRegexPattern"
        }
        return null
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

    private val emailRegexPattern: String
        get() = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

    private val usernameRegexPattern: String
        get() = "^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9])" +
                "{${minUsernameLength - 2},${maxUsernameLength - 2}}[a-zA-Z0-9]\$"

    private val passwordRegexPattern: String
        get() = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$!%*#?&]{$minPasswordLength,$maxPasswordLength}\$"
}
