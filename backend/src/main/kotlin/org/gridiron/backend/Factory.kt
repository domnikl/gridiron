package org.gridiron.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import org.gridiron.backend.model.GameRepository
import org.gridiron.backend.model.TeamRepository
import org.gridiron.backend.model.UserRepository
import org.jetbrains.exposed.sql.Database
import java.util.concurrent.TimeUnit

@KtorExperimentalAPI
class Factory(private val config: ApplicationConfig) {
    val db by lazy {
        val connection = config.propertyOrNull("db.connection")?.getString()
            ?: throw IllegalArgumentException("'db.connection' must be configured")
        val driver = config.propertyOrNull("db.driver")?.getString()
            ?: throw IllegalArgumentException("'db.driver' must be configured")

        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = connection
        hikariConfig.driverClassName = driver
        hikariConfig.maxLifetime = TimeUnit.MINUTES.toMillis(10)
        hikariConfig.isAutoCommit = false

        val username = config.propertyOrNull("db.username")?.getString()
        val password = config.propertyOrNull("db.password")?.getString()

        if (username != null && password != null) {
            hikariConfig.username = username
            hikariConfig.password = password
        }

        hikariConfig.validate()

        Database.connect(HikariDataSource(hikariConfig))
    }

    val jwtAuthentication by lazy {
        JwtAuthentication(
            config.property("jwt.domain").getString(),
            config.property("jwt.audience").getString(),
            config.property("jwt.realm").getString(),
            config.property("jwt.secret").getString()
        )
    }

    val teamRepository by lazy { TeamRepository(db) }
    val gameRepository by lazy { GameRepository(db, teamRepository) }
    val userRepository by lazy { UserRepository(db) }
}
