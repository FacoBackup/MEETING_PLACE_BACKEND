package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.RequestSimple
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object UserDeleteDAO {

    fun delete(data: RequestSimple): Status {
        return try{
            transaction {
                User.deleteWhere { User.id eq data.userID }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}