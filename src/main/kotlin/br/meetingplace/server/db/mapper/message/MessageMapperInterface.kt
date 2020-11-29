package br.meetingplace.server.db.mapper.message

import br.meetingplace.server.modules.message.dto.MessageDTO
import br.meetingplace.server.modules.message.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

interface MessageMapperInterface {
    fun mapMessage(it: ResultRow):MessageOpinionsDTO
    fun mapMessageOpinions(it: ResultRow):MessageDTO
}