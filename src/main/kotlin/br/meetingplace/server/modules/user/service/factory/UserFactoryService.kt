package br.meetingplace.server.modules.user.service.factory

import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.users.UserCreationDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.format.DateTimeFormat
import org.postgresql.util.PSQLException
import java.util.*


object UserFactoryService{

    fun create(data: UserCreationDTO, userDAO: UI): Status {
        return try{
            if(userDAO.readAll(name = data.userName, null,
               phoneNumber = data.phoneNumber,null,null).isEmpty())
                userDAO.create(data)
            else
                Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (e: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}