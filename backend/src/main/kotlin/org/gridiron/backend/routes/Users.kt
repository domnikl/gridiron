package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.sessions.sessions
import org.gridiron.backend.JwtAuthentication
import org.gridiron.backend.model.TeamAlreadyExistsException
import org.gridiron.backend.model.User
import org.gridiron.backend.model.UserRepository
import java.util.*

fun Route.users(userRepository: UserRepository, jwtAuthentication: JwtAuthentication, cookieName: String) {
    post("/auth") {
        val credentials = call.receive<Credentials>()

        userRepository.authenticate(credentials.username, credentials.password)?.let { user ->
            call.sessions.set(cookieName, jwtAuthentication.create(user))

            call.respond(HttpStatusCode.OK, mapOf(
                "uuid" to user.uuid,
                "username" to user.username,
                "email" to user.email,
                "isAdmin" to user.isAdmin
            ))

            return@post
        }

        call.sessions.clear(cookieName)

        call.respond(HttpStatusCode.Unauthorized)
    }

    delete("/auth") {
        call.sessions.clear(cookieName)
        call.respond(HttpStatusCode.OK)
    }

    get("/auth") {
        (call.sessions.get(cookieName) as String?)?.let { jwt ->
            jwtAuthentication.check(jwt)?.subject?.let { uuid ->
                val user = userRepository.find(UUID.fromString(uuid))

                call.respond(HttpStatusCode.OK, mapOf(
                        "uuid" to user.uuid,
                        "username" to user.username,
                        "email" to user.email,
                        "isAdmin" to user.isAdmin
                ))

                return@get
            }
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
