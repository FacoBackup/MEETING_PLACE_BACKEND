package br.meetingplace.server.modules.user.services.delete

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import io.ktor.http.*

object UserDeleteService {

    fun delete(data: RequestUser, userDAO: UI): HttpStatusCode {
        return try{

            if(userDAO.read(data.userID) != null)
                userDAO.delete(data.userID)
            else
                HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}