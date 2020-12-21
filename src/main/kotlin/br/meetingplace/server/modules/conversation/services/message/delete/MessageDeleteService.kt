package br.meetingplace.server.modules.conversation.services.message.delete

import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessage
import io.ktor.http.*

object MessageDeleteService {

    fun deleteMessage(requester: String, data: RequestMessage, messageDAO: MI): HttpStatusCode {
        return try {
            val message = messageDAO.read(messageID = data.messageID)
            if(message != null && message.creatorID == requester)
                messageDAO.delete(data.messageID)
            else
                HttpStatusCode.FailedDependency

        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}