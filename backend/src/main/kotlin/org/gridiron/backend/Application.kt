package org.gridiron.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.content.CachingOptions
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.contentType
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(StatusPages) {
        // TODO: configure
    }
    install(ContentNegotiation) {
        jackson {
            // Configure Jackson's ObjectMapper here
        }
    }
    install(CachingHeaders) {
        options { outgoingContent ->
            if (outgoingContent.contentType?.withoutParameters()?.match(ContentType.Image.Any) == true) {
                CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 30 * 24 * 60 * 60))
            } else {
                null
            }
        }
    }

    routing {
        get("/users") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
