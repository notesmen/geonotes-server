package org.geonotes.server.core.exceptions

import java.lang.RuntimeException


class BadRequestException(message: String) : RuntimeException(message)
