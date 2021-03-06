package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.KtorExperimentalAPI
import org.gridiron.backend.JwtAuthentication
import org.gridiron.backend.model.User
import org.gridiron.backend.model.UserAlreadyExistsException
import org.gridiron.backend.model.UserRepository
import org.gridiron.backend.respondException
import org.mindrot.jbcrypt.BCrypt

@KtorExperimentalAPI
fun Route.users(userRepository: UserRepository, jwtAuthentication: JwtAuthentication) {
    authenticate {
        get("/users") {
            call.respond(
                userRepository.all().map {
                    mapOf(
                        "username" to it.username,
                        "score" to it.score
                    )
                }
            )
        }
    }

    post("/auth") {
        val credentials = call.receive<Credentials>()

        userRepository.authenticate(credentials.username, credentials.password)?.let { user ->
            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "uuid" to user.uuid,
                    "username" to user.username,
                    "email" to user.email,
                    "isAdmin" to (User.Role.ADMIN in user.roles),
                    "jwt" to jwtAuthentication.create(user)
                )
            )

            return@post
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
                BCrypt.gensalt(),
                newUser.email
            )

            if (userRepository.exists(user)) {
                throw UserAlreadyExistsException(user)
            }

            userRepository.save(user)

            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        } catch (e: IllegalArgumentException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: UserAlreadyExistsException) {
            call.respondException(HttpStatusCode.Conflict, e)
        }
    }
}

data class UserPostBody(val username: String, val password: String, val email: String)
data class Credentials(val username: String, val password: String, val rememberMe: Boolean)
