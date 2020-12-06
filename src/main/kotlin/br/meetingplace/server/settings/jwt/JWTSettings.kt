package br.meetingplace.server.settings.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTSettings {
    private const val issuer = "aeb.intranet"
    private const val secret = "QUVCLUlOVFJBTkVULUFQSQ=="
    private const val validityInMs = 36_000_00 * 168
    private val algorithm = Algorithm.HMAC384(secret)


    val jwtVerifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun makeToken(userID: String): String?{

        return try{
            JWT.create()
                .withSubject("Authentication")
                .withIssuer(issuer)
                .withExpiresAt(getExpiration())
                .withClaim("userID", userID)
                .sign(algorithm)
        }catch (e: Exception){
            null
        }
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}