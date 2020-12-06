package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.message.dto.response.MessageDTO
import io.ktor.http.*

interface MI {
    fun create(data: RequestMessageCreation): HttpStatusCode
    fun read(messageID: String): MessageDTO?
    fun check(messageID: String): Boolean
    fun readAllConversation(userID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun readAllFromCreator(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): HttpStatusCode
}