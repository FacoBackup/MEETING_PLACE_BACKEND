package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.RequestSimple
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserDeleteDAO {

    fun delete(data: RequestSimple): Status {
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