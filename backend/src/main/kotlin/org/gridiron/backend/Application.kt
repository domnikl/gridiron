package org.gridiron.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.content.CachingOptions
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import org.gridiron.backend.routes.auth

@KtorExperimentalAPI
fun Application.module() {
    val factory = Factory(environment.config)
    val jwtAuthentication = factory.jwtAuthentication
    val cookieName = "SESSION"

    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(StatusPages) {
        // TODO: configure
    }
    install(ContentNegotiation) {
        jackson {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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
    install(Authentication) {
        jwt {
            jwtAuthentication.provider(this)
        }
    }

    install(Sessions) {
        cookie<String>(cookieName)
    }

    routing {
        auth(jwtAuthentication, cookieName)
    }
}
