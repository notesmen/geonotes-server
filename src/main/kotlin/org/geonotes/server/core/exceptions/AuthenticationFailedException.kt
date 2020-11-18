package org.geonotes.server.core.exceptions

import java.lang.RuntimeException


class AuthenticationFailedException : RuntimeException {
    constructor() : super("Internal error")
    constructor(username: String) : super("Authentication for user '$username' failed. Wrong username/password")
}
