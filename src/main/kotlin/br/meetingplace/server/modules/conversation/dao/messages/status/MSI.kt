package br.meetingplace.server.modules.conversation.dao.messages.status

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import io.ktor.http.*

interface MSI {
    suspend fun create(conversationID: Long, userID: Long, messageID: Long): HttpStatusCode
    suspend fun readAllUnseenMessages(conversationID: Long, userID: Long): List<MessageStatusDTO>
    suspend fun seenByEveryoneByMessage(messageID: Long, conversationID: Long): Boolean
    suspend fun update(conversationID: Long, userID: Long , messageID: Long): HttpStatusCode
    suspend fun unseenMessagesCount(conversationID: Long, userID: Long): Long
}