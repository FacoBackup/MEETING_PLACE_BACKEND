package br.meetingplace.server.modules.user.services.delete

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.dto.requests.RequestUser

object UserDeleteService {

    fun delete(data: RequestUser, userDAO: UI): Status {
        return try{

            if(userDAO.read(data.userID) != null)
                userDAO.delete(data.userID)
            else
                Status(404, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}