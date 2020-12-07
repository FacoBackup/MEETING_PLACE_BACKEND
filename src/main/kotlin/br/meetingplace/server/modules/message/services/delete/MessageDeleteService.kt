package br.meetingplace.server.modules.message.services.delete

import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.requests.RequestMessage
import io.ktor.http.*

object MessageDeleteService {

    fun deleteMessage(requester: String,data: RequestMessage, messageDAO: MI): HttpStatusCode {
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