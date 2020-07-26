package org.gridiron.backend.model

import org.gridiron.backend.persistence.Games
import org.gridiron.backend.persistence.Games.start
import org.gridiron.backend.persistence.Games.team1
import org.gridiron.backend.persistence.Games.team2
import org.gridiron.backend.persistence.Games.uuid
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class GameRepository(private val db: Database, private val teamRepository: TeamRepository) {
    fun generateId(): UUID {
        lateinit var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (exists(uuid))

        return uuid
    }

    private fun exists(uuid: UUID): Boolean {
        return transaction {
            Games.select { Games.uuid.eq(uuid) }.count() > 0
        }
    }

    fun all() = transaction(db) {
        Games.selectAll().map {
            Game(
                it[uuid],
                teamRepository.find(it[team1]),
                teamRepository.find(it[team2]),
                it[start]
            )
        }
    }

    fun save(game: Game) {
        return transaction(db) {
            Games.insertIgnore {
                it[uuid] = game.uuid
                it[team1] = game.team1.uuid
                it[team2] = game.team2.uuid
                it[start] = game.start
            }
        }
    }
}
