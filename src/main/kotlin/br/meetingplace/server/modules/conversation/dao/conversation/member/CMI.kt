package br.meetingplace.server.modules.conversation.dao.conversation.member

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import io.ktor.http.*

interface CMI {
    suspend fun create(userID: String, conversationID: String, admin: Boolean): HttpStatusCode
    suspend fun read(userID: String, conversationID: String): ConversationMemberDTO?
    suspend fun readAllByUser(userID: String): List<ConversationMemberDTO>
    suspend fun readAllByConversation(conversationID: String): List<ConversationMemberDTO>
    suspend fun check(conversationID: String, userID: String): Boolean
    suspend fun update(userID: String, conversationID: String, admin: Boolean): HttpStatusCode
    suspend fun delete(userID: String, conversationID: String): HttpStatusCode
}