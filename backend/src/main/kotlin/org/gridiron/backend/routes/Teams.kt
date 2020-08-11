package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import org.gridiron.backend.model.Team
import org.gridiron.backend.model.TeamAlreadyExistsException
import org.gridiron.backend.model.TeamRepository
import org.gridiron.backend.respondException
import java.util.UUID

fun Route.teams(teamRepository: TeamRepository) {
    get("/teams") {
        call.respond(teamRepository.all())
    }

    patch("/teams/{teamId}") {
        try {
            val newTeam = call.receive<TeamBody>()
            val id = UUID.fromString(call.parameters["teamId"])
            val team = Team(id, newTeam.name)

            teamRepository.save(team)

            call.respond(HttpStatusCode.NoContent)
        } catch (e: IllegalArgumentException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: TeamAlreadyExistsException) {
            call.respondException(HttpStatusCode.Conflict, e)
        }
    }

    post("/teams") {
        try {
            val newTeam = call.receive<TeamBody>()
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

data class TeamBody(val name: String)
