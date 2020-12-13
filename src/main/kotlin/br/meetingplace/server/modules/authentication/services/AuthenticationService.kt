package br.meetingplace.server.modules.authentication.services

import br.meetingplace.server.methods.hashString
import br.meetingplace.server.modules.authentication.dao.ALI
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.settings.jwt.JWTSettings
import io.ktor.http.*

object AuthenticationService {

    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: ALI): String? {
        return try {
            val user = userDAO.readAuthUser(data.userID)

            if(user != null && user.password == hashString(encryption = "SHA-1",data.password)){
                val status = authenticationDAO.create(userID = data.userID, ip = data.ip)

                if (status == HttpStatusCode.OK) JWTSettings.makeToken(userID = user.userID, data.ip)
                else null
            }else null
        }catch (e: Exception){
            null
        }
    }

    fun logout(requester: String, ip: String, userDAO: UserDAO, authenticationDAO: ALI): HttpStatusCode{
        return try {
            val logged = authenticationDAO.read(requester, ip)
            if(userDAO.check(requester) && logged != null && logged.active){
                authenticationDAO.delete(userID = requester, ip = ip)
            }else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}