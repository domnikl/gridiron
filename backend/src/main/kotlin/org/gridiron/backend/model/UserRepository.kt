package org.gridiron.backend.model

import org.gridiron.backend.persistence.Users
import org.gridiron.backend.persistence.Users.email
import org.gridiron.backend.persistence.Users.isAdmin
import org.gridiron.backend.persistence.Users.password
import org.gridiron.backend.persistence.Users.username
import org.gridiron.backend.persistence.Users.uuid
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserRepository(private val db: Database) {
    fun generateId(): UUID {
        lateinit var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (exists(uuid))

        return uuid
    }

    private fun exists(uuid: UUID): Boolean {
        return transaction { byUuid(uuid) } != null
    }

    private fun exists(user: User): Boolean {
        return transaction {
            Users.select { username.eq(user.username) or email.eq(user.email) }.count() > 0
        }
    }

    fun save(user: User) {
        if (exists(user)) {
            throw UserAlreadyExistsException(user)
        }

        return transaction(db) {
            Users.insert {
                it[uuid] = user.uuid
                it[email] = user.email
                it[username] = user.username
                it[password] = user.password
            }
        }
    }

    fun authenticate(username: String, password: String): User? {
        val user = transaction { byUsername(username) }?.map() ?: return null

        return if (user.authenticate(password)) user else null
    }

    fun find(id: UUID): User {
        return transaction { byUuid(id) }?.map() ?: throw UserNotFoundException(id)
    }

    private fun ResultRow.map() = User(this[uuid], this[username], this[password], this[email], this[isAdmin])

    private fun byUuid(uuid: UUID) = Users.select { Users.uuid.eq(uuid) and Users.active.eq(true) }.singleOrNull()
    private fun byUsername(username: String) = Users.select { Users.username.eq(username) and Users.active.eq(true) }.singleOrNull()
}

class UserNotFoundException(uuid: UUID?) :
    RuntimeException("User could not be found '$uuid'")

class UserAlreadyExistsException(user: User) :
    RuntimeException("User '${user.username}' already exists")
