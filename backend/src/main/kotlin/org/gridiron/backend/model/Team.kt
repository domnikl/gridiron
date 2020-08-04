package org.gridiron.backend.model

import java.util.UUID

data class Team(val uuid: UUID, val name: String) {
    init {
        require(name.length >= 3) { "Name must not be shorter than 3 chars" }
        require(name.length <= 100) { "Name must not be longer than 100 chars" }
    }
}
