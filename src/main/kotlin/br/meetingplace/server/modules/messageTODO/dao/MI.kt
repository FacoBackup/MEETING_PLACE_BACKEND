package br.meetingplace.server.modules.messageTODO.dao

import br.meetingplace.server.modules.messageTODO.dto.response.MessageDTO
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestMessageCreation
import br.meetingplace.server.response.status.Status

interface MI {
    fun create(data: RequestMessageCreation, isGroup: Boolean): Status
    fun readAll(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): Status
}