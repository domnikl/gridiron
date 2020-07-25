package org.gridiron.backend

import org.gridiron.backend.Teams.name
import org.gridiron.backend.Teams.uuid
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class TeamRepository(private val db: Database) {
    fun generateId(): UUID {
        lateinit var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (exists(uuid))

        return uuid
    }

    private fun exists(uuid: UUID): Boolean {
        return transaction {
            Teams.select { Teams.uuid.eq(uuid) }.count() > 0
        }
    }

    private fun exists(team: Team): Boolean {
        return transaction {
            Teams.select { Teams.name.eq(team.name) }.count() > 0
        }
    }

    fun all() = transaction(db) {
        Teams.selectAll().map {
            Team(it[uuid], it[name])
        }
    }

    fun save(team: Team) {
        if (exists(team)) {
            throw TeamAlreadyExistsException(team)
        }

        return transaction(db) {
            Teams.insertIgnore {
                it[uuid] = team.uuid
                it[name] = team.name
            }
        }
    }
}

data class Team(val uuid: UUID, val name: String)

class TeamAlreadyExistsException(team: Team) :
    RuntimeException("Team '${team.name}' already exists")
