package br.meetingplace.server.modules.conversation.dao.messages

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import io.ktor.http.*

interface MI {
    suspend fun create(message: String, imageURL: String?, conversationID: String, creator: String, messageID: String): HttpStatusCode
    suspend fun read(messageID: String): MessageDTO?
    suspend fun check(messageID: String): Boolean
    suspend fun readByPage(conversationID: String, page: Int): List<MessageDTO>

    suspend fun readAllConversation(userID: String, conversationID: String): List<MessageDTO>
    suspend fun delete(messageID: String): HttpStatusCode
    suspend fun update(messageID: String): HttpStatusCode
}