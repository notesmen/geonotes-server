package org.geonotes.server.core.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Note(
    id: ObjectId,
    val owner: String,
    val noteId: Long,
    val title: String,
    val text: String,
    val color: Int,
    val lastChangeTime: Long,
    val tags: List<String>
): AbstractDocument(id)
