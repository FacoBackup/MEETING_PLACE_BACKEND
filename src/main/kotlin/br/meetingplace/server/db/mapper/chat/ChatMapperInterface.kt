package br.meetingplace.server.db.mapper.chat

import br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto.ChatDTO
import br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto.ChatOwnerDTO
import br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto.MessageDTO
import br.meetingplace.server.modules.chatTODOTRANSACTIONS.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

interface ChatMapperInterface {
    fun mapChat(it: ResultRow): ChatDTO
    fun mapChatOwners(it: ResultRow):ChatOwnerDTO
    fun mapMessage(it: ResultRow):MessageOpinionsDTO
    fun mapMessageOpinions(it: ResultRow):MessageDTO
}