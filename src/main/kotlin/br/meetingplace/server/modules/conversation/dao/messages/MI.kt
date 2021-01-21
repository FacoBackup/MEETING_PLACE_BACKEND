package br.meetingplace.server.modules.conversation.dao.messages

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import io.ktor.http.*

interface MI {
    suspend fun create(message: String, imageURL: String?, conversationID: Long, creator: Long): Long?
    suspend fun read(messageID: Long): MessageDTO?
    suspend fun check(messageID: Long): Boolean
    suspend fun readByPage(conversationID: Long, page: Long): List<MessageDTO>
    suspend fun readLastPage(conversationID: Long): List<MessageDTO>
    suspend fun readLastMessage(conversationID: Long, userID: Long): String?
    suspend fun readAllConversation(userID: Long, conversationID: Long): List<MessageDTO>
    suspend fun delete(messageID: Long): HttpStatusCode
    suspend fun update(messageID: Long): HttpStatusCode
}