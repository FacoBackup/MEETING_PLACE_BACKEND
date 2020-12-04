package br.meetingplace.server.modules.message.services.delete

import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.requests.RequestMessage

object MessageDeleteService {

    fun deleteMessage(data: RequestMessage, messageDAO: MI): Status {
        return try {
            val message = messageDAO.read(messageID = data.messageID)
            if(message != null && message.creatorID == data.userID)
                messageDAO.delete(data.messageID)
            else
                Status(401, StatusMessages.UNAUTHORIZED)

        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}