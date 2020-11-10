package org.geonotes.server.core.exceptions

class UserNotFound(username: String) : RuntimeException("User '$username' not found")