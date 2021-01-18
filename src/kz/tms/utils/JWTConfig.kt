package kz.tms.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTConfig(jwtProperties: Properties) {

    val realm = jwtProperties["realm"].toString()

    private val secret = jwtProperties["secret"].toString()
    private val issuer = jwtProperties["issuer"].toString()
    private val audience = jwtProperties["audience"].toString()

    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun makeToken(username: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("username", username)
        .withExpiresAt(getExpiresDate())
        .sign(algorithm)

    private fun getExpiresDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 12)
        return calendar.time
    }
}