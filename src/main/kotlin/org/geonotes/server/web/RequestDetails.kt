package org.geonotes.server.web

data class RegistrationRequest(val username: String,
                               val password: String,
                               val email: String)

data class AuthenticationRequest(val username: String,
                                 val password: String)
