package org.gridiron.backend.model

import java.util.UUID

data class Team(val uuid: UUID, val name: String, val logo: ByteArray?) {
    init {
        require(name.length >= 3) { "Name must not be shorter than 3 chars" }
        require(name.length <= 100) { "Name must not be longer than 100 chars" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Team

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (logo != null) {
            if (other.logo == null) return false
            if (!logo.contentEquals(other.logo)) return false
        } else if (other.logo != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (logo?.contentHashCode() ?: 0)
        return result
    }
}
