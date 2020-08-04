package org.gridiron.backend.model

import org.gridiron.backend.persistence.Teams
import org.gridiron.backend.persistence.Teams.name
import org.gridiron.backend.persistence.Teams.uuid
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
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
            Teams.select { Teams.name.eq(team.name) }.count() > 0
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
        }?.let {
            Team(it[uuid], it[name])
        } ?: throw TeamNotFoundException(id)
    }

    private fun byUuid(uuid: UUID) = Teams.select { Teams.uuid.eq(uuid) }
}

class TeamNotFoundException(uuid: UUID) :
    RuntimeException("Team could not be found '$uuid'")

class TeamAlreadyExistsException(team: Team) :
    RuntimeException("Team '${team.name}' already exists")
