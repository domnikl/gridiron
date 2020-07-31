package org.gridiron.backend.model

import org.joda.time.DateTime
import java.util.UUID

data class Game(val uuid: UUID, val team1: Team, val team2: Team, val start: DateTime, val bets: MutableList<Bet>) {
    fun placeBet(user: User, away: Int, home: Int) {
        bets.add(Bet(user.uuid, away, home))
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

data class Bet(val user: UUID, val away: Int, val home: Int)
