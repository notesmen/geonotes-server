package org.geonotes.server.web

import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder

import org.geonotes.server.core.NotesManager
import org.geonotes.server.logger


@RestController
@RequestMapping("/api/notes")
class NoteController {
    @GetMapping("list")
    fun getNotesList(
        @RequestParam(name = "only-changed-after", required = false)
        onlyChangedAfter: Long?
    ): ResponseEntity<Any?> {
        log.debug("Reached getNotesList")
        val owner: String = retrieveUsername()
        return notesManager.getNotesList(owner, onlyChangedAfter ?: 0)
    }

    @PostMapping("download")
    fun retrieveNotes(@RequestBody request: DownloadNotesRequest): ResponseEntity<Any?> {
        log.debug("Reached retrieveNotes")
        validateDownloadRequest(request)?.let { error ->
            return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        }
        val owner: String = retrieveUsername()
        return notesManager.retrieveNotes(owner, request.noteIds)
    }

    @PostMapping("upload")
    fun saveNotes(@RequestBody request: UploadNotesRequest): ResponseEntity<Any?> {
        log.debug("Reached saveNotes")
        validateUploadRequest(request)?.let { error ->
            return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        }
        val owner: String = retrieveUsername()
        return notesManager.saveNotes(owner, request.notes)
    }

    private fun validateDownloadRequest(request: DownloadNotesRequest): String? {
        if (request.noteIds.size > maxNotesPerRequest) {
            return "You cant request more than $maxNotesPerRequest notes at once"
        } else if (request.noteIds.isEmpty()) {
            return "Request is empty"
        }
        return null
    }

    private fun validateUploadRequest(request: UploadNotesRequest): String? {
        for (note in request.notes) {
            if (note.title.length !in 1..maxTitleLength) {
                return "note.title length must be in [1, $maxTitleLength]"
            }
            if (note.text.length !in 0..maxTextLength) {
                return "note.text length must be in [1, $maxTextLength]"
            }
            if (note.tags.sumBy { it.length } !in 0..maxTagsFieldLength) {
                return "note.tags field is too large in total (must be in [1, $maxTagsFieldLength])"
            }
        }
        return null
    }


    private fun retrieveUsername() = SecurityContextHolder.getContext().authentication.name

    @Autowired
    private lateinit var notesManager: NotesManager

    @Value("\${api.max-notes-per-request}")
    private var maxNotesPerRequest: Int = 0

    @Value("\${api.max-title-length}")
    private var maxTitleLength: Int = 0

    @Value("\${api.max-text-length}")
    private var maxTextLength: Int = 0

    @Value("\${api.max-tags-total-length}")
    private var maxTagsFieldLength: Int = 0

    private val log by logger()
}
