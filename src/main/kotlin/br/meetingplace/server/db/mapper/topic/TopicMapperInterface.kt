package br.meetingplace.server.db.mapper.topic

import br.meetingplace.server.modules.topic.dto.TopicDTO
import br.meetingplace.server.modules.topic.dto.TopicOpinionsDTO
import br.meetingplace.server.modules.user.dto.UserDTO
import org.jetbrains.exposed.sql.ResultRow

interface TopicMapperInterface {
    fun mapTopic(it: ResultRow): TopicDTO
    fun mapTopicOpinions(it: ResultRow):TopicOpinionsDTO
}