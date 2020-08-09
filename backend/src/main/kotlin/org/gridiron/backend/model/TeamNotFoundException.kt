package org.gridiron.backend.model

import java.util.UUID

class TeamNotFoundException(message: String): RuntimeException(message) {
    companion object {
        fun fromUuid(uuid: UUID) = TeamNotFoundException("Team could not be found '$uuid'")
        fun fromName(name: String) = TeamNotFoundException("Team could not be found by name '$name'")
    }
}
