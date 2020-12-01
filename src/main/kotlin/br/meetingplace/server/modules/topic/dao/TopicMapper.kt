package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.topic.entitie.TopicOpinions
import br.meetingplace.server.modules.topic.dto.TopicDTO
import br.meetingplace.server.modules.topic.dto.TopicOpinionsDTO
import org.jetbrains.exposed.sql.ResultRow

object TopicMapper: TopicMapperInterface {
    override fun mapTopic(it: ResultRow): TopicDTO {
        return TopicDTO(id =  it[Topic.id], header =  it[Topic.header], body =  it[Topic.body], approved =  it[Topic.approved], footer =  it[Topic.footer], creatorID =  it[Topic.creatorID], mainTopicID =  it[Topic.mainTopicID], creationDate =  it[Topic.creationDate].toString("dd-MM-yyyy"), communityID = it[Topic.communityID], imageURL = it[Topic.imageURL])
    }

    override fun mapTopicOpinions(it: ResultRow): TopicOpinionsDTO {
        return TopicOpinionsDTO(liked =  it[TopicOpinions.liked], userID =  it[TopicOpinions.userID], topicID = it[TopicOpinions.topicID])
    }
}