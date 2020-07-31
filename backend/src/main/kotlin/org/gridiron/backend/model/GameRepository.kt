package org.gridiron.backend.model

import org.gridiron.backend.persistence.Bets
import org.gridiron.backend.persistence.Games
import org.gridiron.backend.persistence.Games.start
import org.gridiron.backend.persistence.Games.team1
import org.gridiron.backend.persistence.Games.team2
import org.gridiron.backend.persistence.Games.uuid
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class GameRepository(
    private val db: Database,
    private val teamRepository: TeamRepository
) {
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
        Games.selectAll().orderBy(start to SortOrder.ASC).map {
            Game(
                it[uuid],
                teamRepository.find(it[team1]),
                teamRepository.find(it[team2]),
                it[start],
                betsByGame(it[uuid]).toMutableList()
            )
        }
    }

    fun save(game: Game) {
        return transaction(db) {
            Games.replace {
                it[uuid] = game.uuid
                it[team1] = game.team1.uuid
                it[team2] = game.team2.uuid
                it[start] = game.start
            }

            game.bets.forEach { bet ->
                Bets.replace {
                    it[user] = bet.user
                    it[Bets.game] = game.uuid
                    it[away] = bet.away
                    it[home] = bet.home
                }
            }
        }
    }

    fun find(id: UUID): Game {
        return transaction {
            byUuid(id).singleOrNull()?.let {
                Game(
                    it[uuid],
                    teamRepository.find(it[team1]),
                    teamRepository.find(it[team2]),
                    it[start],
                    betsByGame(id).toMutableList()
                )
            }
        } ?: throw GameNotFoundException(id)
    }

    private fun byUuid(uuid: UUID) = Games.select { Games.uuid.eq(uuid ) }
    private fun betsByGame(uuid: UUID) = Bets.select { Bets.game.eq(uuid) }.map {
        Bet(
            it[Bets.user],
            it[Bets.away],
            it[Bets.home]
        )
    }
}

class GameNotFoundException(uuid: UUID) :
    RuntimeException("Game could not be found '$uuid'")
