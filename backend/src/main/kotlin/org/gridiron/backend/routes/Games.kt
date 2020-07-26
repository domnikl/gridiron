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

fun Route.games(gameRepository: GameRepository, teamRepository: TeamRepository) {
    get("/games") {
        call.respond(gameRepository.all())
    }

    post("/games") {
        try {
            val newTeam = call.receive<NewGame>()
            val id = gameRepository.generateId()
            val team1 = teamRepository.find(newTeam.team1)
            val team2 = teamRepository.find(newTeam.team2)

            val game = Game(id, team1, team2, newTeam.start)

            gameRepository.save(game)

            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        } catch (e: TeamAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict, mapOf("message" to e.message))
        }
    }
}

data class NewGame(val team1: UUID, val team2: UUID, val start: DateTime)
