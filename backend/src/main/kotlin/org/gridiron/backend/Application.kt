package org.gridiron.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.main() {
    val server = embeddedServer(Netty, port = 4001) {
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
        }
    }

    server.start(wait = true)
}
