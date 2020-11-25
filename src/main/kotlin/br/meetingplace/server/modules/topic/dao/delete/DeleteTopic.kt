package br.meetingplace.server.modules.topic.dao.delete

import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

object DeleteTopic{

    fun deleteTopic(data:TopicSimpleOperator): Status{
        return try {
            Topic.deleteWhere { (Topic.id eq data.topicID) and (Topic.creatorID eq data.userID)}
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}