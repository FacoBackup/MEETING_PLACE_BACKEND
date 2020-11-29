package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.db.mapper.topic.TopicMapperInterface
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.topic.db.TopicOpinions
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicOpinionDAO{

    fun dislike(data: TopicSimpleOperator, topicMapper: TopicMapperInterface): Status {
        return try {
            return if (transaction { !User.select { User.id eq data.userID }.empty() } &&
                    transaction { !Topic.select { Topic.id eq data.topicID }.empty() })
                 when (checkLikeDislike(data.topicID, userID = data.userID, topicMapper = topicMapper)) {
                    0 -> {
                        transaction {
                            TopicOpinions.update({ (TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID) }) {
                                it[liked] = false
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    }// like to dislike
                    1 -> {
                        transaction {
                            TopicOpinions.deleteWhere { (TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID) }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    }
                    2 -> {
                        transaction {
                            TopicOpinions.insert {
                                it[userID] = data.userID
                                it[topicID] = data.topicID
                                it[liked] = false
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    }
                    else -> Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun like(data: TopicSimpleOperator, topicMapper: TopicMapperInterface): Status {
        return try {
            if (transaction { !User.select { User.id eq data.userID }.empty() } && transaction { !Topic.select { Topic.id eq data.topicID }.empty() })
                when (checkLikeDislike(data.topicID, userID = data.userID, topicMapper = topicMapper)) {
                    0 -> { // like to null
                        transaction {
                            TopicOpinions.deleteWhere {(TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID)}
                        }

                        Status(statusCode = 200, StatusMessages.OK)
                    }
                    1 -> {  // dislike to like
                        transaction {
                            TopicOpinions.update ({(TopicOpinions.userID eq data.userID) and (TopicOpinions.topicID eq data.topicID)}){
                                it[liked] = true
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    }
                    2 -> { // nothing yet
                        transaction {
                            TopicOpinions.insert {
                                it[userID] = data.userID
                                it[topicID] = data.topicID
                                it[liked] = true
                            }
                        }
                        Status(statusCode = 200, StatusMessages.OK)
                    }
                    else-> Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    private fun checkLikeDislike(topicID: String, topicMapper: TopicMapperInterface, userID: String): Int {
        val opinions = transaction {
            TopicOpinions.select { (TopicOpinions.userID eq userID) and (TopicOpinions.topicID eq topicID)}.map { topicMapper.mapTopicOpinions(it)}.firstOrNull()
        }

        return when{
            opinions == null -> 2 // 2 hasn't DISLIKED or liked yet
            opinions.liked -> 0
            !opinions.liked -> 1 // 1 ALREADY DISLIKED
            else -> 2
        }
    }
}