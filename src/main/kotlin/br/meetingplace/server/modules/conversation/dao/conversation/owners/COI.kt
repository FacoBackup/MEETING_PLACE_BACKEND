package br.meetingplace.server.modules.conversation.dao.conversation.owners

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationOwnersDTO
import io.ktor.http.*

interface COI {
    suspend fun create(userID: String, secondUserID: String, conversationID: String): HttpStatusCode
    suspend fun read(userID: String, secondUserID: String): ConversationOwnersDTO?
    suspend fun check(userID: String, secondUserID: String): Boolean
    suspend fun readAll(userID: String): List<ConversationOwnersDTO>
}