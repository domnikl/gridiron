package org.gridiron.backend.model

import org.gridiron.backend.persistence.Teams
import org.gridiron.backend.persistence.Teams.name
import org.gridiron.backend.persistence.Teams.uuid
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TeamRepository(private val db: Database) {
    fun generateId(): UUID {
        lateinit var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (exists(uuid))

        return uuid
    }

    private fun exists(uuid: UUID): Boolean {
        return transaction { byUuid(uuid).count() > 0 }
    }

    private fun exists(team: Team): Boolean {
        return transaction {
            Teams.select { name.eq(team.name) and not(uuid.eq(team.uuid)) }.count() > 0
        }
    }

    fun all() = transaction(db) {
        Teams.selectAll().orderBy(name to SortOrder.ASC).map {
            Team(it[uuid], it[name])
        }
    }

    fun save(team: Team) {
        if (exists(team)) {
            throw TeamAlreadyExistsException(team)
        }

        return transaction(db) {
            Teams.replace {
                it[uuid] = team.uuid
                it[name] = team.name
            }
        }
    }

    fun find(id: UUID): Team {
        return transaction {
            byUuid(id).singleOrNull()
        }?.map() ?: throw TeamNotFoundException.fromUuid(id)
    }

    fun findByName(name: String): Team {
        return transaction {
            Teams.select { Teams.name.eq(name) }.singleOrNull()
        }?.map() ?: throw TeamNotFoundException.fromName(name)
    }

    private fun byUuid(uuid: UUID) = Teams.select { Teams.uuid.eq(uuid) }

    private fun ResultRow.map(): Team {
        return Team(this[uuid], this[name])
    }
}
