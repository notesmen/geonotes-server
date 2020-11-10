package org.geonotes.server.core

import java.util.Optional
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository


interface TokenRepository : MongoRepository<Token, ObjectId> {
    fun findTokenByValue(value: String): Optional<Token>
    fun findTokensByOwnerUsername(username: String): Optional<Token>
}