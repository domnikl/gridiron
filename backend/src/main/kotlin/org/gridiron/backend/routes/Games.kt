package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.gridiron.backend.model.*
import org.joda.time.DateTime
import java.util.*

fun Route.games(gameRepository: GameRepository, teamRepository: TeamRepository, userRepository: UserRepository) {
    get("/games") {
        call.respond(gameRepository.all())
    }

    post("/games") {
        try {
            val newTeam = call.receive<NewGame>()
            val id = gameRepository.generateId()
            val team1 = teamRepository.find(newTeam.team1)
            val team2 = teamRepository.find(newTeam.team2)

            val game = Game.create(id, team1, team2, newTeam.start)

            gameRepository.save(game)

            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, mapOf("message" to e.message))
        }
    }

    post("/games/{gameId}/bets") {
        try {
            // TODO: get user uuid from token
            val user = userRepository.find(UUID.randomUUID())

            val body = call.receive<BetBody>()
            val game = gameRepository.find(UUID.fromString(call.parameters["gameId"]))

            game.placeBet(user, body.away, body.home)

            gameRepository.save(game)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("message" to e.message))
        }
    }
}

data class NewGame(val team1: UUID, val team2: UUID, val start: DateTime)
data class BetBody(val away: Int, val home: Int)
