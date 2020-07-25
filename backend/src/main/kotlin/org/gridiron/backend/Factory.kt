package org.gridiron.backend

import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
class Factory(private val config: ApplicationConfig) {
    val jwtAuthentication by lazy {
        JwtAuthentication(
                config.property("jwt.domain").getString(),
                config.property("jwt.audience").getString(),
                config.property("jwt.realm").getString(),
                config.property("jwt.secret").getString()
        )
    }
}
