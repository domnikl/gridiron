package org.gridiron.backend

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.CachingHeaders
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import org.gridiron.backend.persistence.Bets
import org.gridiron.backend.persistence.Games
import org.gridiron.backend.persistence.Teams
import org.gridiron.backend.persistence.Users
import org.gridiron.backend.routes.games
import org.gridiron.backend.routes.teams
import org.gridiron.backend.routes.users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration

suspend fun ApplicationCall.respondException(httpStatusCode: HttpStatusCode, e: Throwable) {
    this.respond(httpStatusCode, mapOf("message" to e.message))
}

@ObsoleteCoroutinesApi
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
            TODO("log uncaught exceptions")
            call.respondException(HttpStatusCode.InternalServerError, cause)
        }*/
    }
    install(ContentNegotiation) {
        jackson {
            registerModule(JodaModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
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

    icsActor(factory.icsImporter)
}

@ObsoleteCoroutinesApi
fun CoroutineScope.icsActor(icsImporter: IcsImporter) = actor<String> {
    while (true) {
        icsImporter.import()
        delay(Duration.ofDays(24))
    }
}
