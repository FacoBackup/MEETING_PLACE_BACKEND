package br.meetingplace.server.modules.user.services.authentication

import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dao.authentication.AI
import br.meetingplace.server.modules.user.dto.requests.RequestLog
import br.meetingplace.server.modules.user.dto.response.AuthenticationStatusDTO
import io.ktor.http.*
import io.ktor.http.content.*
import java.security.MessageDigest

object AuthenticationService {

    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): AuthenticationStatusDTO {
        return try {
            val user = userDAO.read(data.userID)
            if(user != null && authenticationDAO.read(data.userID) == null &&
               user.password == MessageDigest.getInstance("MD5").digest(data.password.toByteArray(Charsets.UTF_8)).toString()){

                val status = authenticationDAO.create(userID = data.userID)
                return if (status.statusCode == HttpStatusCode.OK)
                    AuthenticationStatusDTO(token = , statusCode = HttpStatusCode.OK)
                else
                    AuthenticationStatusDTO(token = , statusCode = HttpStatusCode.InternalServerError)
            }else AuthenticationStatusDTO(token = , statusCode = HttpStatusCode.InternalServerError)
        }catch (e: Exception){
            AuthenticationStatusDTO(token = , statusCode = HttpStatusCode.InternalServerError)
        }
    }

    fun logoff(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): HttpStatusCode{
        return try {
            val user = userDAO.read(data.userID)
            if(user != null && authenticationDAO.read(data.userID) != null && user.password == data.password){
                authenticationDAO.delete(userID = data.userID)
            }else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}