package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.dto.MessageDTO
import br.meetingplace.server.modules.message.dto.MessageOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

interface MessageMapperInterface {
    fun mapMessageOpinions(it: ResultRow):MessageOpinionsDTO
    fun mapMessage(it: ResultRow):MessageDTO
}