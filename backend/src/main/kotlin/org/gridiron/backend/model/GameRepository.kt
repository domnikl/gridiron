package org.gridiron.backend.model

import org.gridiron.backend.persistence.Bets
import org.gridiron.backend.persistence.Games
import org.gridiron.backend.persistence.Games.scoreAway
import org.gridiron.backend.persistence.Games.scoreHome
import org.gridiron.backend.persistence.Games.start
import org.gridiron.backend.persistence.Games.team1
import org.gridiron.backend.persistence.Games.team2
import org.gridiron.backend.persistence.Games.uuid
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.UUID

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

    fun exists(start: DateTime, away: Team, home: Team): Boolean {
        return transaction {
            Games.select {
                team1.eq(away.uuid) and team2.eq(home.uuid) and Games.start.eq(start)
            }.count() > 0
        }
    }

    private fun exists(uuid: UUID): Boolean {
        return transaction {
            Games.select { Games.uuid.eq(uuid) }.count() > 0
        }
    }

    fun all() = transaction(db) {
        Games.selectAll().orderBy(start to SortOrder.ASC).map { it.map() }
    }

    fun save(game: Game) {
        return transaction(db) {
            Games.replace {
                it[uuid] = game.uuid
                it[team1] = game.team1.uuid
                it[team2] = game.team2.uuid
                it[start] = game.start
                it[scoreAway] = game.score?.away
                it[scoreHome] = game.score?.home
            }

            game.bets.forEach { bet ->
                Bets.replace {
                    it[user] = bet.user
                    it[Bets.game] = game.uuid
                    it[away] = bet.score.away
                    it[home] = bet.score.home
                }
            }
        }
    }

    fun find(id: UUID): Game {
        return transaction {
            byUuid(id).singleOrNull()?.map()
        } ?: throw GameNotFoundException(id)
    }

    private fun ResultRow.map() = Game(
        this[uuid],
        teamRepository.find(this[team1]),
        teamRepository.find(this[team2]),
        this[start],
        betsByGame(this[uuid]).toMutableList(),
        this[scoreAway]?.let { Score(it, this[scoreHome]!!) }
    )

    private fun byUuid(uuid: UUID) = Games.select { Games.uuid.eq(uuid) }
    private fun betsByGame(uuid: UUID): List<Game.Bet> = Bets.select { Bets.game.eq(uuid) }.map {
        Game.Bet(
            it[Bets.user],
            Score(it[Bets.away], it[Bets.home])
        )
    }
}
