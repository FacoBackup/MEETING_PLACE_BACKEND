package br.meetingplace.server.modules.user.service.delete

import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.generic.LogDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserDelete {

    fun delete(data: LogDTO): Status {
        return try{

            if(transaction { !User.select { User.id eq data.userID }.empty() }){
                transaction {
                    User.deleteWhere { User.id eq data.userID }
                }
                Status(200, StatusMessages.OK)
            }
            else
                Status(404, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}