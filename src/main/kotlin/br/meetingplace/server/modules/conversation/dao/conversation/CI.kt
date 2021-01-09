package br.meetingplace.server.modules.conversation.dao.conversation

import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationDTO
import io.ktor.http.*

interface CI {
    suspend fun create(data: RequestConversationCreation, id: String): HttpStatusCode
    suspend fun read(conversationID: String): ConversationDTO?
    suspend fun check(conversationID: String): Boolean
    suspend fun update(conversationID: String, latestMessage: Long?, name: String?, about: String?, imageURL: String?): HttpStatusCode
    suspend fun delete(conversationID: String): HttpStatusCode
    suspend fun readByName(input: String, userID: String):List<ConversationDTO>
}