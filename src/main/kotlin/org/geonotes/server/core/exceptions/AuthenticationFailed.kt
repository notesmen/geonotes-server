package org.geonotes.server.core.exceptions

import java.lang.RuntimeException

class AuthenticationFailed() : RuntimeException("Password dont match")