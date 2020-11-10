package org.geonotes.server.core.exceptions

import java.lang.RuntimeException

class UserAlreadyExists(username: String) : RuntimeException("Username '$username' is busy")