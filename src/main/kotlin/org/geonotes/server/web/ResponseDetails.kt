package org.geonotes.server.web

data class AuthenticationResponse(val token: String)

data class NoteDescription(val noteId: Long, val lastChangeTime: Long)

data class NotesListResponse(val noteDescriptions: Array<NoteDescription>)


data class NoteResponse(val noteId: Long,
                        val title: String,
                        val text: String,
                        val color: Int,
                        val lastChangeTime: Long,
                        val tags: List<String>)

data class DownloadNotesResponse(val notes: Array<NoteResponse>)
