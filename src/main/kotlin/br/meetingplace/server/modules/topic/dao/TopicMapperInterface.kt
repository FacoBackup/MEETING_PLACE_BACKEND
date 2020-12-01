package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.dto.TopicDTO
import br.meetingplace.server.modules.topic.dto.TopicOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

interface TopicMapperInterface {
    fun mapTopic(it: ResultRow): TopicDTO
    fun mapTopicOpinions(it: ResultRow):TopicOpinionsDTO
}