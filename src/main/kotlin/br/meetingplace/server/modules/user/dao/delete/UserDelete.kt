package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.data.Login
import org.jetbrains.exposed.sql.deleteWhere

object UserDelete {

    fun delete(login: Login): Status {
        return try{
            User.deleteWhere { User.id eq login.email }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}