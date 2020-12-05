package br.meetingplace.server.modules.authentication.services

import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.authentication.dao.AI
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.dto.response.AuthenticationStatusDTO
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import io.ktor.http.content.*
import java.security.MessageDigest

object AuthenticationService {

    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): AuthenticationStatusDTO {
        TODO()
    //        return try {
//            val user = userDAO.read(data.userID)
//            if(user != null && authenticationDAO.read(data.userID) == null &&
//               user.password == MessageDigest.getInstance("SHA-1").digest(data.password.toByteArray(Charsets.UTF_8)).toString()){
//                val status = authenticationDAO.create(userID = data.userID, ip = data.ip)
//                return if (status == HttpStatusCode.OK)
//                    AuthenticationStatusDTO(token = , statusCode = HttpStatusCode.OK)
//                else
//                    AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.InternalServerError)
//            }else AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.InternalServerError)
//        }catch (e: Exception){
//            AuthenticationStatusDTO(token = null, statusCode = HttpStatusCode.InternalServerError)
//        }
    }

    fun logoff(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): HttpStatusCode{
        return try {
            val user = userDAO.readAuthUser(data.userID)
            if(user != null && authenticationDAO.read(data.userID) != null && user.password == MessageDigest.getInstance("SHA-1").digest(data.password.toByteArray(Charsets.UTF_8)).toString()){
                authenticationDAO.update(userID = data.userID, ip = data.ip)
            }else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
//    private fun makeToken(userID: String, )
    fun checkToken(token: String, userDAO: UI): HttpStatusCode{
        TODO()
    }
}