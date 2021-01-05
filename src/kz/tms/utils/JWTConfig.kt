package kz.tms.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import kz.tms.database.data.user.User
import java.util.*

class JWTConfig(jwtProperties: HashMap<String, String>) {
    private val secret = jwtProperties["secret"]
    private val realm = jwtProperties["realm"]
    private val issuer = jwtProperties["issuer"]
    private val audience = jwtProperties["audience"]

    private val algorithm = Algorithm.HMAC256(secret)

    fun makeToken(user: User): String = JWT
        .create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("username", user.username)
        .withClaim("password", user.password)
        .withExpiresAt(setAndReturnDate(Calendar.HOUR, 12))
        .sign(algorithm)

    fun makeJwtVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()
}