package org.gridiron.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.gridiron.backend.model.User

class JwtAuthentication(
    val realm: String,
    private val issuer: String,
    private val audience: String,
    secret: String
) {
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier =  JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    fun create(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withSubject(user.uuid.toString())
            .sign(algorithm)
    }
}
