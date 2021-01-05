package kz.tms.database.data.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import kz.tms.utils.setAndReturnDate
import java.util.*

private val algorithm = Algorithm.HMAC256("super secret")

val jwtRealm = "realm"
val jwtIssuer = "issuer"
val jwtAudience = "audience"

fun makeToken(username: String, password: String): String = JWT
    .create()
    .withSubject("Authentication")
    .withIssuer(jwtIssuer)
    .withAudience(jwtAudience)
    .withClaim("username", username)
    .withClaim("password", password)
    .withExpiresAt(setAndReturnDate(Calendar.HOUR, 12))
    .sign(algorithm)

fun makeJwtVerifier(): JWTVerifier = JWT
    .require(algorithm)
    .withIssuer(jwtIssuer)
    .withAudience(jwtAudience)
    .build()