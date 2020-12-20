package br.meetingplace.server.modules.conversation.dao.messages.status

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import io.ktor.http.*

interface MSI {
    fun create(conversationID: String, userID: String, messageID: String, seen: Boolean): HttpStatusCode
    fun readUnseen(conversationID: String, userID: String): List<MessageStatusDTO>
    fun update(conversationID: String, userID: String , messageID: String): HttpStatusCode
}