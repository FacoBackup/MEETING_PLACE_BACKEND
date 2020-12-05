package br.meetingplace.server.modules.user.services.delete

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import io.ktor.http.*

object UserDeleteService {

    fun delete(data: RequestUser, userDAO: UI): HttpStatusCode {
        return try{
            val requester = userDAO.read(data.requester)
            if( requester != null && requester.admin)
                userDAO.delete(data.userID)
            else
                HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}