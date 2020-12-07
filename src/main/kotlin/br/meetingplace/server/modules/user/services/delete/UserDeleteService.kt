package br.meetingplace.server.modules.user.services.delete

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import io.ktor.http.*

object UserDeleteService {

    fun delete(requester: String, data: RequestUser, userDAO: UI): HttpStatusCode {
        return try{
            val userRequester = userDAO.read(requester)
            if( userRequester != null && userRequester.admin)
                userDAO.delete(data.requestedUser)
            else
                HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}