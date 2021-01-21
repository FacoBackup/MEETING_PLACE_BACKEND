package br.meetingplace.server.modules.conversation.dao.conversation.member

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import io.ktor.http.*

interface CMI {
    suspend fun create(userID: Long, conversationID: Long, admin: Boolean): HttpStatusCode
    suspend fun read(userID: Long, conversationID: Long): ConversationMemberDTO?
    suspend fun readAllByUser(userID: Long): List<ConversationMemberDTO>
    suspend fun readAllByConversation(conversationID: Long): List<ConversationMemberDTO>
    suspend fun check(conversationID: Long, userID: Long): Boolean
    suspend fun update(userID: Long, conversationID: Long, admin: Boolean): HttpStatusCode
    suspend fun delete(userID: Long, conversationID: Long): HttpStatusCode
}