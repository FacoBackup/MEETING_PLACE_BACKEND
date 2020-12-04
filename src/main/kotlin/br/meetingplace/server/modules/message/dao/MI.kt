package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.dto.response.MessageDTO
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation

interface MI {
    fun create(data: RequestMessageCreation): Status
    fun read(messageID: String): MessageDTO?
    fun readAllConversation(userID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun readAllFromCreator(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): Status
}