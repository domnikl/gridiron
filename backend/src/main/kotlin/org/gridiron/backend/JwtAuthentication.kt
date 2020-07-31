package org.gridiron.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.auth.jwt.JWTAuthenticationProvider
import io.ktor.auth.jwt.JWTPrincipal
import org.gridiron.backend.model.User
import java.time.temporal.ChronoUnit
import java.util.*

class JwtAuthentication(
    private val realm: String,
    private val issuer: String,
    private val audience: String,
    private val secret: String
) {
    fun provider(configuration: JWTAuthenticationProvider.Configuration) {
        val verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build()

        configuration.realm = realm
        configuration.verifier(verifier)
        configuration.validate { credential ->
            if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
        }
    }

    fun create(user: User): String {
        val expiresAt = Date().toInstant().plus(24, ChronoUnit.HOURS)

        return JWT.create()
            .withExpiresAt(Date.from(expiresAt))
            .withAudience(audience)
            .withIssuer(issuer)
            .withSubject(user.uuid.toString())
            .sign(Algorithm.HMAC256(secret))
    }

    fun check(token: String): DecodedJWT? = try {
        JWT.decode(token)
    } catch (e: JWTDecodeException) {
        null;
    }
}
