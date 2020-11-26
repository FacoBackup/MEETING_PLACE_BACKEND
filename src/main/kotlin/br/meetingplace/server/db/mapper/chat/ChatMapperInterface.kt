package br.meetingplace.server.db.mapper.chat

import br.meetingplace.server.modules.chat.dto.ChatDTO
import br.meetingplace.server.modules.chat.dto.ChatOwnerDTO
import br.meetingplace.server.modules.chat.dto.MessageDTO
import br.meetingplace.server.modules.chat.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

interface ChatMapperInterface {
    fun mapChat(it: ResultRow): ChatDTO
    fun mapChatOwners(it: ResultRow):ChatOwnerDTO
    fun mapMessage(it: ResultRow):MessageOpinionsDTO
    fun mapMessageOpinions(it: ResultRow):MessageDTO
}