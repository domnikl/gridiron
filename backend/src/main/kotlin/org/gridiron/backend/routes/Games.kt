package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import org.gridiron.backend.model.*
import org.joda.time.DateTime
import java.util.*

fun Route.games(gameRepository: GameRepository, teamRepository: TeamRepository, userRepository: UserRepository) {
    get("/games") {
        call.respond(gameRepository.all())
    }

    post("/games/{gameId}/bets") {
        try {
            val userId = call.principal<JWTPrincipal>()?.payload?.subject ?: throw UserNotFoundException(null)
            val user = userRepository.find(UUID.fromString(userId))

            val body = call.receive<BetBody>()
            val game = gameRepository.find(UUID.fromString(call.parameters["gameId"]))

            game.placeBet(user, body.score)

            gameRepository.save(game)

            call.respond(HttpStatusCode.Created)
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: GameAlreadyStartedException) {
            call.respond(HttpStatusCode.PreconditionFailed, mapOf("message" to e.message))
        }
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
            call.respond(HttpStatusCode.InternalServerError, mapOf("message" to e.message))
        }
    }

    patch("/games/{gameId}") {
        try {
            val body = call.receive<PatchBody>()
            val game = gameRepository.find(UUID.fromString(call.parameters["gameId"]))
            val scores = game.end(body.score)

            scores.forEach { (userId, score) ->
                val user = userRepository.find(userId)
                user.scored(score)
                userRepository.save(user)
            }

            gameRepository.save(game)

            call.respond(HttpStatusCode.OK, mapOf("scores" to scores))
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.NotFound, mapOf("message" to e.message))
        } catch (e: GameNotFoundException) {
            call.respond(HttpStatusCode.NotFound, mapOf("message" to e.message))
        } catch (e: GameAlreadyEndedException) {
            call.respond(HttpStatusCode.PreconditionFailed, mapOf("message" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("message" to e.message))
        }
    }
}

data class NewGame(val team1: UUID, val team2: UUID, val start: DateTime)

data class BetBody(val away: Int, val home: Int) {
    val score = Score(away, home)
}

data class PatchBody(val away: Int, val home: Int) {
    val score = Score(away, home)
}
