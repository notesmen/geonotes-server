package org.geonotes.server.core

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

class Token(id: ObjectId,
            @Indexed
            val value: String,
            @CreatedDate
            val lastAccessTime: Date,
            val ownerUsername: String
) : AbstractDocument(id)