package br.meetingplace.server.modules.conversation.dao.conversation

import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationDTO
import io.ktor.http.*

interface CI {
    suspend fun create(data: RequestConversationCreation): Long?
    suspend fun read(conversationID: Long): ConversationDTO?
    suspend fun readNewest(minID: Long, requester: Long):List<ConversationDTO>
    suspend fun check(conversationID: Long): Boolean
    suspend fun update(conversationID: Long, latestMessage: Boolean, name: String?, about: String?, imageURL: String?): HttpStatusCode
    suspend fun delete(conversationID: Long): HttpStatusCode
    suspend fun readByName(input: String, userID: Long):List<ConversationDTO>
}