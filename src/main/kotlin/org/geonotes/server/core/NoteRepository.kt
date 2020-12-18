package org.geonotes.server.core

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

import org.geonotes.server.core.model.Note

@Component
interface NoteRepository : MongoRepository<Note, ObjectId> {
    fun findNotesByOwner(owner: String): List<Note>
    fun findNotesByOwnerAndLastChangeTimeIsGreaterThanEqual(owner: String, lastChangeTime: Long): List<Note>

    fun findNotesByOwnerAndNoteIdIn(owner: String, noteIds: LongArray): List<Note>
}
