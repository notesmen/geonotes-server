package org.geonotes.server.core

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    id: ObjectId,
    val username: String,
    val passwordHash: String,
    var email: String
) : AbstractDocument(id)