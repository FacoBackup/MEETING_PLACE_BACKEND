package br.meetingplace.server.modules.authentication.services

import br.meetingplace.server.methods.hashString
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.authentication.dao.AI
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.dto.response.AuthenticationStatusDTO
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import io.ktor.http.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.jodatime.Date
import org.joda.time.DateTime
import java.security.MessageDigest
import java.time.Instant
import java.util.*

object AuthenticationService {
    private const val issuer = "aeb.intranet"

    private const val secret = ("c2VjcmV0")

    private val algorithm = Algorithm.HMAC384(secret)


    val jwtVerifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()


    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): AuthenticationStatusDTO {
        return try {
        val user = userDAO.readAuthUser(data.userID)
        
        if(user != null && user.password == hashString(encryption = "SHA-1",data.password)){
            val status = authenticationDAO.create(userID = data.userID, ip = data.ip)
            return if (status == HttpStatusCode.OK)
                AuthenticationStatusDTO(token = makeToken(userID = user.userID, password = user.password), statusCode = HttpStatusCode.OK)
            else
                AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.InternalServerError)
        }else AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.FailedDependency)
    }catch (e: Exception){
        AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.InternalServerError)
    }
    }

    fun logout(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): HttpStatusCode{
        return try {
            val user = userDAO.readAuthUser(data.userID)
            if(user != null && authenticationDAO.read(data.userID) != null && user.password == MessageDigest.getInstance("SHA-1").digest(data.password.toByteArray(Charsets.UTF_8)).toString()){
                authenticationDAO.update(userID = data.userID, ip = data.ip)
            }else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun checkToken(token: String, userID: String): HttpStatusCode{
        return try{
            val decodedToken = JWT.decode(token).payload
            if(decodedToken.contains(userID))
                HttpStatusCode.OK
            else HttpStatusCode.Unauthorized
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    private fun makeToken(userID: String, password: String): String?{

        return try{
            JWT.create()
                .withSubject("Authentication")
                .withIssuer(issuer)
                //.withExpiresAt()
                .withClaim("userID", userID)
                .withClaim("password", password)
                .sign(algorithm)
        }catch (e: Exception){
            null
        }
    }
}