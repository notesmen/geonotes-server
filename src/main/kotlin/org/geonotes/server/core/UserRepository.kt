package org.geonotes.server.core

import org.bson.types.ObjectId
import org.geonotes.server.core.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component


@Component
interface UserRepository : MongoRepository<User, ObjectId> {
    fun findUserById(id: ObjectId): User?
    fun findUserByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}
