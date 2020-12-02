package br.meetingplace.server.modules.user.service.delete

import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.generic.LogDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserDeleteService {

    fun delete(data: LogDTO, userDAO: UI): Status {
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