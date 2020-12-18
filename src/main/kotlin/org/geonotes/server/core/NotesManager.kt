package org.geonotes.server.core

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.http.ResponseEntity

import org.geonotes.server.core.model.Note
import org.geonotes.server.web.*


@Component
class NotesManager {
    fun getNotesList(owner: String, lastChangeTime: Long): ResponseEntity<Any?> {
        val noteDescriptions: Array<NoteDescription> =
            noteRepository.findNotesByOwnerAndLastChangeTimeIsGreaterThanEqual(owner, lastChangeTime)
                .map { NoteDescription(it.noteId, it.lastChangeTime) }
                .toTypedArray()
        return ResponseEntity.ok(NotesListResponse(noteDescriptions))
    }

    fun retrieveNotes(owner: String, noteIds: LongArray): ResponseEntity<Any?> {
        val notes: Array<NoteResponse> =
            noteRepository.findNotesByOwnerAndNoteIdIn(owner, noteIds)
                .map { NoteResponse(it.noteId, it.title, it.text, it.color, it.lastChangeTime, it.tags) }
                .toTypedArray()
        return ResponseEntity.ok(DownloadNotesResponse(notes))
    }

    fun saveNotes(owner: String, notes: Array<NoteRequest>): ResponseEntity<Any?> {
        noteRepository.saveAll(
            notes.map { note ->
                Note(
                    ObjectId.get(),
                    owner,
                    note.noteId,
                    note.title,
                    note.text,
                    note.color,
                    note.lastChangeTime,
                    note.tags
                )
            }
        )
        return ResponseEntity.ok("Created")
    }

    @Autowired
    private lateinit var noteRepository: NoteRepository
}
