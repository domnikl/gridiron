package org.gridiron.backend.model

import org.gridiron.backend.persistence.Users
import org.gridiron.backend.persistence.Users.email
import org.gridiron.backend.persistence.Users.password
import org.gridiron.backend.persistence.Users.roles
import org.gridiron.backend.persistence.Users.score
import org.gridiron.backend.persistence.Users.username
import org.gridiron.backend.persistence.Users.uuid
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository(private val db: Database) {
    fun generateId(): UUID {
        lateinit var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (transaction { byUuid(uuid) } != null)

        return uuid
    }

    fun exists(user: User): Boolean {
        return transaction {
            Users.select { username.eq(user.username) or email.eq(user.email) }.count() > 0
        }
    }

    fun save(user: User) {
        return transaction(db) {
            Users.replace {
                it[uuid] = user.uuid
                it[email] = user.email
                it[username] = user.username
                it[password] = user.password
                it[score] = user.score
                it[roles] = user.roles.joinToString(", ")
            }
        }
    }

    fun authenticate(username: String, password: String): User? {
        val user = transaction { byUsername(username) }?.mapRow() ?: return null

        return if (User.Role.USER in user.roles && user.authenticate(password)) {
            user
        } else {
            null
        }
    }

    fun find(id: UUID): User {
        return transaction { byUuid(id) }?.mapRow() ?: throw UserNotFoundException(id)
    }

    fun all(): List<User> = transaction {
        Users.selectAll()
            .orderBy(score to SortOrder.DESC)
            .map { it.mapRow() }
    }

    private fun ResultRow.mapRow() = User(
        this[uuid],
        this[username],
        this[password],
        this[email],
        this[score],
        this[roles].split(',').map { it.trim() }.filter { it.isNotBlank() }.map {
            User.Role.valueOf(it)
        }.toSet()
    )

    private fun byUuid(uuid: UUID) = Users.select { Users.uuid.eq(uuid) }.singleOrNull()

    private fun byUsername(username: String) = Users.select { Users.username.eq(username) }.singleOrNull()
}
