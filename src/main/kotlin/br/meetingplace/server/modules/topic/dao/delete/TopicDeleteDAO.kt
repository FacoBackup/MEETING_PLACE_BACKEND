package br.meetingplace.server.modules.topic.dao.delete

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object TopicDeleteDAO{

    fun deleteTopic(data:TopicSimpleOperator): Status {
        return try {
            transaction {
                Topic.deleteWhere { (Topic.id eq data.topicID) and (Topic.creatorID eq data.userID)}
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}