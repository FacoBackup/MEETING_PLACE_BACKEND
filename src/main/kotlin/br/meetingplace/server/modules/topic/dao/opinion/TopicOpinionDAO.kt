package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.TopicOpinionDTO
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.topic.entitie.TopicOpinions
import br.meetingplace.server.modules.topic.service.opinion.TopicOpinion
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object TopicOpinionDAO:TOI {

    override fun create(topicID: String, userID: String, liked: Boolean): Status {
        return try {
            transaction {
                TopicOpinions.insert {
                    it[this.topicID]  = topicID
                    it[this.userID] = userID
                    it[this.liked] = liked
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(topicID: String, userID: String): Status {
        return try {
            transaction {
                TopicOpinions.deleteWhere {
                    (TopicOpinions.topicID eq topicID) and
                    (TopicOpinions.userID eq userID)
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun readAll(topicID: String): List<TopicOpinionDTO> {
        return try {
            transaction {
                TopicOpinions.select {
                    TopicOpinions.topicID eq topicID
                }.map { mapTopicOpinions(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override fun update(topicID: String, userID: String, liked: Boolean): Status {
        return try {
            transaction {
                TopicOpinions.update({
                    (TopicOpinions.topicID eq topicID) and
                 (TopicOpinions.userID eq userID)
                }){
                    it[this.liked] = liked
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapTopicOpinions(it: ResultRow): TopicOpinionDTO {
        return TopicOpinionDTO(liked =  it[TopicOpinions.liked], userID =  it[TopicOpinions.userID], topicID = it[TopicOpinions.topicID])
    }
}