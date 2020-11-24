package br.meetingplace.server.db.mapper.topic

import br.meetingplace.server.modules.topic.dto.TopicDTO
import br.meetingplace.server.modules.topic.dto.TopicOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

class TopicMapper: TopicMapperInterface {
    override fun mapTopic(it: ResultRow): TopicDTO {
        TODO("Not yet implemented")
    }

    override fun mapTopicOpinions(it: ResultRow): TopicOpinionsDTO {
        TODO("Not yet implemented")
    }
}