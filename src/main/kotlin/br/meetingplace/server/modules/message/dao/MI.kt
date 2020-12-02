package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.group.dto.GroupDTO
import br.meetingplace.server.modules.message.dto.MessageDTO
import br.meetingplace.server.modules.message.dto.MessageOpinionsDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import br.meetingplace.server.request.dto.message.MessageCreationDTO
import br.meetingplace.server.response.status.Status
import org.jetbrains.exposed.sql.ResultRow

interface MI {
    fun create(data: MessageCreationDTO, isGroup: Boolean): Status
    fun readAll(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO>
    fun delete(messageID: String): Status
}