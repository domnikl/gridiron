package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.sessions
import org.gridiron.backend.JwtAuthentication
import org.gridiron.backend.model.TeamAlreadyExistsException
import org.gridiron.backend.model.User
import org.gridiron.backend.model.UserRepository

fun Route.users(userRepository: UserRepository, jwtAuthentication: JwtAuthentication, cookieName: String) {
    post("/users/login") {
        // TODO: what if this fails?
        val credentials = call.receive<Credentials>()

        if (credentials.username == "liebler.dominik@gmail.com" && credentials.password == "foobar") {
            val jwt = jwtAuthentication.create()

            call.sessions.set(cookieName, jwt)
            call.respond(HttpStatusCode.NoContent)
        }

        call.respond(HttpStatusCode.Unauthorized)
    }

    post("/users") {
        try {
            val newUser = call.receive<UserPostBody>()
            val id = userRepository.generateId()

            val user = User.register(
                id,
                newUser.username,
                newUser.password,
                newUser.email
            )

            userRepository.save(user)

            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        } catch (e: TeamAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict, mapOf("message" to e.message))
        }
    }
}

data class UserPostBody(val username: String, val password: String, val email: String)
data class Credentials(val username: String, val password: String, val rememberMe: Boolean)
