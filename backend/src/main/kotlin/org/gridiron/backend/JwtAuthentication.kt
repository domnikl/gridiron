package org.gridiron.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.JWTAuthenticationProvider
import io.ktor.auth.jwt.JWTPrincipal
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

    fun create(): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .sign(Algorithm.HMAC256(secret))
    }
}
