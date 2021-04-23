package kz.seasky.tms.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTConfig(properties: Properties) {

    val realm = properties["realm"].toString()

    private val secret = properties["secret"].toString()
    private val issuer = properties["issuer"].toString()
    private val audience = properties["audience"].toString()

    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun makeToken(id: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim(JWT_CLAIM_ID, id)
        .withExpiresAt(getExpireDate())
        .sign(algorithm)

    private fun getExpireDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 12)
        return calendar.time
    }
}