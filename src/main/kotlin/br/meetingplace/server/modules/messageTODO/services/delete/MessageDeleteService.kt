package br.meetingplace.server.modules.messageTODO.services.delete

import br.meetingplace.server.modules.messageTODO.entities.Message
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestMessage
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object MessageDeleteService {

    fun deleteMessage(data: RequestMessage): Status {
        return try {
            if(transaction { !Message.select { (Message.id eq data.messageID) and (Message.creatorID eq data.userID)}.empty() }){
                transaction {
                    Message.deleteWhere { (Message.id eq data.messageID) and (Message.creatorID eq data.userID)}
                }
                Status(200, StatusMessages.OK)
            }
            else
                Status(401, StatusMessages.UNAUTHORIZED)

        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}