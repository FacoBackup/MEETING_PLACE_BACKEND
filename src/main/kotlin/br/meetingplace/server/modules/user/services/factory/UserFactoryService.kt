package br.meetingplace.server.modules.user.services.factory

import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import org.postgresql.util.PSQLException


object UserFactoryService{

    fun create(data: RequestUserCreation, userDAO: UI): Status {
        return try{
            if(userDAO.readAllByAttribute(name = data.userName, null,
               phoneNumber = data.phoneNumber,null,null).isEmpty())
                userDAO.create(data)
            else
                Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (e: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}