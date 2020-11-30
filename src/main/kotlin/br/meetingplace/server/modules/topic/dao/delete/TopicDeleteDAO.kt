package br.meetingplace.server.modules.topic.dao.delete

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.requests.topics.RequestTopicSimple
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object TopicDeleteDAO{

    fun deleteTopic(data: RequestTopicSimple): Status {
        return try {
            if(transaction { !Topic.select { (Topic.id eq data.topicID) and (Topic.creatorID eq data.userID)}.empty() }){
                transaction {
                    Topic.deleteWhere { (Topic.id eq data.topicID) and (Topic.creatorID eq data.userID)}
                }
                Status(200, StatusMessages.OK)
            }
            else
                Status(401, StatusMessages.UNAUTHORIZED)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}