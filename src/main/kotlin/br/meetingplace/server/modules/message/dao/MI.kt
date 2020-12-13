package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.message.dto.response.MessageDTO
import io.ktor.http.*

interface MI {
    fun create(message: String, imageURL: String?, to: String, from: String, isGroup: Boolean): HttpStatusCode
    fun read(messageID: String): MessageDTO?
    fun check(messageID: String): Boolean
    fun readAllConversation(userID: String, subjectID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): HttpStatusCode
}