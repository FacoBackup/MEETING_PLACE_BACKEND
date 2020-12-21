package br.meetingplace.server.modules.conversation.dao.messages

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import io.ktor.http.*

interface MI {
    fun create(message: String, imageURL: String?, conversationID: String, creator: String, messageID: String): HttpStatusCode
    fun read(messageID: String): MessageDTO?
    fun check(messageID: String): Boolean
    fun readAllConversation(userID: String, conversationID: String): List<MessageDTO>
    fun delete(messageID: String): HttpStatusCode
    fun update(messageID: String): HttpStatusCode
}