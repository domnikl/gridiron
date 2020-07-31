package org.gridiron.backend

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.CachingOptions
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import org.gridiron.backend.persistence.Bets
import org.gridiron.backend.persistence.Games
import org.gridiron.backend.persistence.Teams
import org.gridiron.backend.persistence.Users
import org.gridiron.backend.routes.users
import org.gridiron.backend.routes.games
import org.gridiron.backend.routes.teams
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
fun Application.module() {
    val factory = Factory(environment.config)
    val jwtAuthentication = factory.jwtAuthentication

    transaction(factory.db) {
        SchemaUtils.createMissingTablesAndColumns(Teams, Games, Users, Bets)
    }

    install(DefaultHeaders)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.Accept)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.UserAgent)
        header(HttpHeaders.Referrer)
        allowCredentials = true
        anyHost()
    }
    install(CallLogging)
    install(Compression)
    install(StatusPages) {
        /*exception<Exception> { cause ->
            call.respond(HttpStatusCode.InternalServerError, mapOf("message" to cause.message))
        }*/
    }
    install(ContentNegotiation) {
        jackson {
            registerModule(JodaModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
            realm = jwtAuthentication.realm
            verifier(jwtAuthentication.verifier)
            validate { JWTPrincipal(it.payload) }
        }
    }

    routing {
        users(factory.userRepository, jwtAuthentication)

        authenticate {
            teams(factory.teamRepository)
            games(factory.gameRepository, factory.teamRepository, factory.userRepository)
        }
    }
}
