package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.TopicOpinionDTO
import br.meetingplace.server.modules.topic.entitie.TopicOpinion
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicOpinionDAO:TOI {

    override fun create(topicID: String, userID: String, liked: Boolean): Status {
        return try {
            transaction {
                TopicOpinion.insert {
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
                TopicOpinion.deleteWhere {
                    (TopicOpinion.topicID eq topicID) and
                    (TopicOpinion.userID eq userID)
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(topicID: String, userID: String): TopicOpinionDTO? {
        return try {
            transaction {
                TopicOpinion.select {
                    (TopicOpinion.topicID eq topicID) and
                    (TopicOpinion.userID eq userID)
                }.map { mapTopicOpinions(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override fun readAll(topicID: String): List<TopicOpinionDTO> {
        return try {
            transaction {
                TopicOpinion.select {
                    TopicOpinion.topicID eq topicID
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
                TopicOpinion.update({
                    (TopicOpinion.topicID eq topicID) and
                 (TopicOpinion.userID eq userID)
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
        return TopicOpinionDTO(liked =  it[TopicOpinion.liked], userID =  it[TopicOpinion.userID], topicID = it[TopicOpinion.topicID])
    }
}