package br.meetingplace.server.modules.topic.dao.like

import br.meetingplace.server.db.mapper.topic.TopicMapperInterface
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.topic.db.TopicOpinions
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import org.jetbrains.exposed.sql.*


object LikeTopic {
    fun like(data: TopicSimpleOperator, topicMapper: TopicMapperInterface): Status {
        return if (!User.select { User.id eq data.userID }.empty() && !Topic.select { Topic.id eq data.topicID }.empty())
            return when (checkLikeDislike(data.topicID, userID = data.userID, topicMapper = topicMapper)) {
                0 -> { // like to null
                    TopicOpinions.deleteWhere {(TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID)}
                    Status(statusCode = 200, StatusMessages.OK)
                }
                1 -> {  // dislike to like
                    TopicOpinions.update ({(TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID)}){
                        it[liked] = true
                    }
                    Status(statusCode = 200, StatusMessages.OK)
                }
                2 -> { // nothing yet
                    TopicOpinions.insert {
                        it[userID] = data.userID
                        it[topicID] = data.topicID
                        it[liked] = true
                    }
                    Status(statusCode = 200, StatusMessages.OK)
                }
                else-> Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
        else Status(statusCode = 404, StatusMessages.NOT_FOUND)
    }

    private fun checkLikeDislike(topicID: String, topicMapper: TopicMapperInterface, userID: String): Int {
        val opinions = TopicOpinions.select { (TopicOpinions.userID eq userID) and (TopicOpinions.topicID eq topicID)}.map { topicMapper.mapTopicOpinions(it)}.firstOrNull()

        return when{
            opinions == null -> 2 // nothing yet
            opinions.liked -> 0 // like to null
            !opinions.liked -> 1 // dislike to like
            else -> 2
        }
    }
}