package br.meetingplace.server.modules.user.services.authentication

import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dao.authentication.AI
import br.meetingplace.server.modules.user.dto.requests.RequestLog
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages

object AuthenticationService {

    fun login(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): Status{
        return try {
            val user = userDAO.read(data.userID)
            if(user != null && authenticationDAO.read(data.userID) == null && user.password == data.password){
                authenticationDAO.create(userID = data.userID)
            }else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun logoff(data: RequestLog, userDAO: UserDAO, authenticationDAO: AI): Status{
        return try {
            val user = userDAO.read(data.userID)
            if(user != null && authenticationDAO.read(data.userID) != null && user.password == data.password){
                authenticationDAO.delete(userID = data.userID)
            }else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}