package br.meetingplace.server.modules.conversation.dao.conversation.owners

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationOwnersDTO
import io.ktor.http.*

interface COI {
    suspend fun create(userID: Long, secondUserID: Long, conversationID: Long): HttpStatusCode
    suspend fun read(userID: Long, secondUserID: Long): ConversationOwnersDTO?
    suspend fun readByConversation(conversationID: Long): ConversationOwnersDTO?
    suspend fun check(userID: Long, secondUserID: Long): Boolean
    suspend fun readAll(userID: Long): List<ConversationOwnersDTO>
}