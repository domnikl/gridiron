package org.gridiron.backend.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.sessions
import org.gridiron.backend.JwtAuthentication

fun Route.auth(jwtAuthentication: JwtAuthentication, cookieName: String) {
    post("/auth/login") {
        // TODO: what if this fails?
        val credentials = call.receive<Credentials>()

        if (credentials.username == "liebler.dominik@gmail.com" && credentials.password == "foobar") {
            val jwt = jwtAuthentication.create()

            call.sessions.set(cookieName, jwt)
            call.respond(HttpStatusCode.NoContent)
        }

        call.respond(HttpStatusCode.Unauthorized)
    }
}

data class Credentials(val username: String, val password: String, val rememberMe: Boolean)
