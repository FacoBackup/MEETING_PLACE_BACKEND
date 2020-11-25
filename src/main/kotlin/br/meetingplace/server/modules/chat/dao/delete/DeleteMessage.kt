package br.meetingplace.server.modules.chat.dao.delete

import br.meetingplace.server.modules.chat.db.Message
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

object DeleteMessage {

    fun deleteMessage(data: ChatSimpleOperator): Status{
        return try {
            Message.deleteWhere { (Message.id eq data.messageID) and (Message.creatorID eq data.userID)}
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}