package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.gridiron.backend.model.Team
import org.gridiron.backend.model.TeamAlreadyExistsException
import org.gridiron.backend.model.TeamRepository
import org.gridiron.backend.respondException

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
        } catch (e: IllegalArgumentException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: TeamAlreadyExistsException) {
            call.respondException(HttpStatusCode.Conflict, e)
        }
    }
}

data class NewTeam(val name: String)
