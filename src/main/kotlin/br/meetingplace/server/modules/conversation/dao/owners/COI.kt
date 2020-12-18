package br.meetingplace.server.modules.conversation.dao.owners

import br.meetingplace.server.modules.conversation.dto.response.ConversationOwnersDTO
import io.ktor.http.*

interface COI {
    fun create(userID: String, secondUserID: String, conversationID: String): HttpStatusCode
    fun read(userID: String, secondUserID: String): ConversationOwnersDTO?
    fun readAll(userID: String): List<ConversationOwnersDTO>
}