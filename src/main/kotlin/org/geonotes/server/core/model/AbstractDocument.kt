package org.geonotes.server.core.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id


open class AbstractDocument(@Id var id: ObjectId = ObjectId.get()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return this.id == (other as AbstractDocument?)?.id
    }

    override fun hashCode(): Int = id.hashCode()
}
