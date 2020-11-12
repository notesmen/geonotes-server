package org.geonotes.server.core.exceptions

import java.lang.RuntimeException

class UserAlreadyExistsException(username: String) : RuntimeException("Username '$username' is busy")