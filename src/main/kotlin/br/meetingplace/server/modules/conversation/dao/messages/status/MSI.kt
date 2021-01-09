package br.meetingplace.server.modules.conversation.dao.messages.status

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import io.ktor.http.*

interface MSI {
    suspend fun create(conversationID: String, userID: String, messageID: String): HttpStatusCode
    suspend fun readUnseen(conversationID: String, userID: String): List<MessageStatusDTO>
    suspend fun seenByEveryoneByMessage(messageID: String, conversationID: String): Boolean
    suspend fun update(conversationID: String, userID: String , messageID: String): HttpStatusCode
    suspend fun unseenMessages(conversationID: String, userID: String): Long
}