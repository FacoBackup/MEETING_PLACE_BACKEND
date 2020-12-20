package br.meetingplace.server.modules.conversation.dao.conversation.member

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import io.ktor.http.*

interface CMI {
    fun create(userID: String, conversationID: String, admin: Boolean): HttpStatusCode
    fun read(userID: String, conversationID: String): ConversationMemberDTO?
    fun readAllByUser(userID: String): List<ConversationMemberDTO>
    fun readAllByConversation(conversationID: String): List<ConversationMemberDTO>
    fun check(conversationID: String, userID: String): Boolean
    fun update(userID: String, conversationID: String, admin: Boolean): HttpStatusCode
    fun delete(userID: String, conversationID: String): HttpStatusCode
}