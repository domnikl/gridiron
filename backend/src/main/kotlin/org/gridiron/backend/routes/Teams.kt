package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.gridiron.backend.Team
import org.gridiron.backend.TeamAlreadyExistsException
import org.gridiron.backend.TeamRepository

fun Route.teams(teamRepository: TeamRepository) {
    get("/teams") {
        call.respond(teamRepository.all())
    }

    post("/teams") {
        try {
            val newTeam = call.receive<NewTeam>()
            val id = teamRepository.generateId()
            val team = Team(id, newTeam.name)

            teamRepository.save(team)

            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        } catch (e: TeamAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict, mapOf("message" to e.message))
        }
    }
}

data class NewTeam(val name: String)
