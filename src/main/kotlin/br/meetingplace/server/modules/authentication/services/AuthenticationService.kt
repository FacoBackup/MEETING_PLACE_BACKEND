package br.meetingplace.server.modules.authentication.services

import br.meetingplace.server.methods.hashString
import br.meetingplace.server.modules.authentication.dao.LI
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.settings.jwt.JWTSettings
import io.ktor.http.*
import java.security.MessageDigest

object AuthenticationService {

    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: LI): String? {
        return try {
            val user = userDAO.readAuthUser(data.userID)

            if(user != null && user.password == hashString(encryption = "SHA-1",data.password)){
                val status = authenticationDAO.create(userID = data.userID, ip = data.ip)

                if (status == HttpStatusCode.OK) JWTSettings.makeToken(userID = user.userID)
                else null
            }else null
        }catch (e: Exception){
            null
        }
    }

    fun logout(data: RequestLog, userDAO: UserDAO, authenticationDAO: LI): HttpStatusCode{
        return try {
            val user = userDAO.readAuthUser(data.userID)
            if(user != null && authenticationDAO.read(data.userID) != null && user.password == MessageDigest.getInstance("SHA-1").digest(data.password.toByteArray(Charsets.UTF_8)).toString()){
                authenticationDAO.update(userID = data.userID, ip = data.ip)
            }else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}