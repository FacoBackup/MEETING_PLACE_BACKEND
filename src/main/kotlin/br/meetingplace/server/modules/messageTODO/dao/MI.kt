package br.meetingplace.server.modules.messageTODO.dao

import br.meetingplace.server.modules.messageTODO.dto.MessageDTO
import br.meetingplace.server.request.dto.message.MessageCreationDTO
import br.meetingplace.server.response.status.Status

interface MI {
    fun create(data: MessageCreationDTO, isGroup: Boolean): Status
    fun readAll(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): Status
}