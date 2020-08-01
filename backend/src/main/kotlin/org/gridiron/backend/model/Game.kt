package org.gridiron.backend.model

import org.joda.time.DateTime
import java.util.UUID
import kotlin.math.absoluteValue

data class Score(val away: Int, val home: Int) {
    val tie = away == home
    val awayWon = away > home && !tie
    val homeWon = home > away && !tie

    operator fun minus(other: Score): Int {
        return (away - other.away).absoluteValue + (home - other.home)
    }
}

data class Game(
    val uuid: UUID,
    val team1: Team,
    val team2: Team,
    val start: DateTime,
    val bets: MutableList<Bet>,
    var score: Score? = null
) {
    fun placeBet(user: User, score: Score) {
        if (start.isBeforeNow) {
            throw GameAlreadyStartedException(this)
        }

        bets.add(Bet(user.uuid, score))
    }

    fun end(score: Score): Map<UUID, Int> {
        if (this.score != null) {
            throw GameAlreadyEndedException(this)
        }

        this.score = score

        val withLowestDiff = bets.map { it.user to it.score - score }.minBy { it.second }

        return bets.map {
            it.user to when {
                this.score == it.score -> 12
                it.user == withLowestDiff?.first -> 6
                this.score!!.tie == it.score.tie -> 3
                this.score!!.awayWon == it.score.awayWon -> 3
                this.score!!.homeWon == it.score.homeWon -> 3
                else -> 0
            }
        }.toMap()
    }

    companion object {
        fun create(uuid: UUID, team1: Team, team2: Team, start: DateTime): Game {
            return Game(
                uuid,
                team1,
                team2,
                start,
                mutableListOf()
            )
        }
    }
}

data class Bet(val user: UUID, val score: Score)

class GameAlreadyStartedException(game: Game) :
    RuntimeException("Game '${game.uuid}' already started")

class GameAlreadyEndedException(game: Game) :
    RuntimeException("Game '${game.uuid}' already ended")
