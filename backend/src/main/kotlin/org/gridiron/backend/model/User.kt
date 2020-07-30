package org.gridiron.backend.model

import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

data class User(val uuid: UUID, val username: String, val password: String, val email: String, val isAdmin: Boolean = false) {
    fun authenticate(password: String): Boolean {
        return BCrypt.checkpw(password, this.password)
    }

    companion object {
        fun register(uuid: UUID, username: String, password: String, email: String): User {
            return User(uuid, username, BCrypt.hashpw(password, BCrypt.gensalt()), email)
        }
    }
}
