package org.gridiron.backend.model

import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

data class User(
    val uuid: UUID,
    val username: String,
    val password: String,
    val email: String,
    var score: Int,
    val isActive: Boolean = false,
    val isAdmin: Boolean = false
) {
    init {
        require(username.length >= 3) { "Username must not be shorter than 3 chars" }
        require(username.length <= 70) { "Username must not be longer than 70 chars" }
        require(email.isNotBlank()) { "Email must not be blank" }
    }

    fun authenticate(password: String): Boolean {
        return BCrypt.checkpw(password, this.password)
    }

    fun scored(score: Int) {
        this.score += score
    }

    companion object {
        fun register(
            uuid: UUID,
            username: String,
            password: String,
            salt: String,
            email: String
        ): User {
            require(password.length >= 8) { "Password must not be shorter than 8 chars" }
            require(salt.isNotBlank()) { "Salt must not be blank" }

            return User(
                uuid,
                username,
                BCrypt.hashpw(password, salt),
                email,
                0
            )
        }
    }
}
