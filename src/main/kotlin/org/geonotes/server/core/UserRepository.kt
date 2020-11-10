package org.geonotes.server.core

import java.util.Optional
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId> {
    fun findUserById(id: ObjectId): Optional<User>
    fun findUserByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean
}