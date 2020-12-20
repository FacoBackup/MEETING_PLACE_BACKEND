package br.meetingplace.server.modules.conversation.dao.conversation.owners

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationOwnersDTO
import io.ktor.http.*

interface COI {
    fun create(userID: String, secondUserID: String, conversationID: String): HttpStatusCode
    fun read(userID: String, secondUserID: String): ConversationOwnersDTO?
    fun check(userID: String, secondUserID: String): Boolean
    fun readAll(userID: String): List<ConversationOwnersDTO>
}