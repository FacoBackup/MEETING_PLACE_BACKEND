package br.meetingplace.server.modules.conversation.dao

import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.response.ConversationDTO
import io.ktor.http.*

interface CI {
    fun create(data: RequestConversationCreation): String?
    fun read(conversationID: String): ConversationDTO?
    fun check(conversationID: String): Boolean
    fun update(conversationID: String, name: String?, about: String?, imageURL: String?): HttpStatusCode
    fun delete(conversationID: String): HttpStatusCode
}